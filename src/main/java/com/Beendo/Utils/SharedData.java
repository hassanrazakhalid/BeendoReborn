package com.Beendo.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.PropertyConfigurator;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.Beendo.Entities.Document;
import com.Beendo.Entities.Practice;
import com.Beendo.Entities.Provider;
import com.Beendo.Entities.User;
import com.Beendo.ExceptionHandlers.SessionExpiredException;
import com.Beendo.Services.EmailSendingCallback;
import com.Beendo.Services.IProviderService;

@Service
public class SharedData implements ApplicationContextAware {

	// private static AtomicReference<SharedData> INSTANCE = new
	// AtomicReference<SharedData>();

	@Autowired
	private AuthenticationManager authenticationManager;
	private final Logger logger = LoggerFactory.getLogger(SharedData.class);
	private static SharedData instance = null;

	private Authentication authentication;
	private JavaMailSenderImpl mail;

	private String documentRootPath;
	private String serverRootPath;

	@Autowired
	private IProviderService providerService;
	// private User currentUser;

	@Autowired
	@Qualifier("sas")
	private SessionAuthenticationStrategy sessionAuthenticationStrategy;

	
	private void init() {

		// addTimerForDocumentExpire();

	}

	public String getDocumentRootPath() {
		return documentRootPath;
	}

	public void setDocumentRootPath(String documentRootPath) {
		this.documentRootPath = documentRootPath;
	}

	public String getServerRootPath() {
		return serverRootPath;
	}

	public void setServerRootPath(String serverRootPath) {
		this.serverRootPath = serverRootPath;
	}

	@PostConstruct
	private void initializeData() {

		// Properties props = new Properties();
		//
		// try {
		// props = readFile("log4j.properties");
		//
		//// props.load(new FileInputStream("/log4j.properties"));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// PropertyConfigurator.configure(props);
		loadPaths();
		makeAllProvidersFolders();
	}

	private void makeAllProvidersFolders() {

		List<Provider> allProviders = providerService.getAll();

		for (Provider provider : allProviders) {

			provider.makeFolderIfNotExist();

		}
	}

	public void run() {

//		checkForUnSendDocumentsReminders();
	}

	private void loadPaths() {

		try {
			Properties dict = readFile("ServerSettings.properties");
			this.setServerRootPath((String) dict.getProperty("serverPath"));
			this.setDocumentRootPath((String) dict.getProperty("documentPath"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkForUnSendDocumentsReminders() {

		Properties map = null;
		logger.debug("In Unsend Doc Logic");
		try {
			map = readFile("EmailSettings.properties");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (map != null) {
			logger.info("Email Prperties is OK");
			// int min = Integer.valueOf(map.getProperty("timerDuration")) ;
			// int totalDelay = min * 1000 * 60;
			String from = (String) map.get("username");

			EmailSendingCallback callBack = (List<Document> documents, List<User> admins) -> {

				boolean shouldUpdate = false;
				logger.info("Documetns to send: " + documents.size());
				logger.info("Users to receive: " + admins.size());

				for (Document document : documents) {

					// send emails to these documents , they are already
					// filtered
					String msg = getMessageBody(document);// document.getOrignalName()
															// + "is going to
															// expired on" +
															// document.getExpireDate()
															// + "<b> BOLD
															// </b>";

					// User admin =
					// getAdminById(admins,document.getProvider().getCentity().getId());

					// String emails =
					// "hassanrazakhalid@yahoo.com,haider.khalid@sypore.com";
					List<String> emails = getEmailsToSend(document.getProvider().getCentity().getId(), admins);
					logger.info("Emails: " + emails);
					if (!emails.isEmpty()) {
						shouldUpdate = true;
						sendMail(from, emails, "Document Reminder", msg);
						// sendMail(from, "Hassan.raza@sypore.com", "Document
						// Reminder", msg);
						document.setReminderStatus(1);
					}
				}

				if (!documents.isEmpty() && shouldUpdate)
					providerService.updateDocuments(documents);

				logger.info(new Date().toString());
				// System.out.println(new Date());
			};

			logger.info("Getting List for documents for sending email");
			providerService.getDocumentByEmail(callBack);

		} else {

			logger.info("Email Prperties is NUll");
		}

	}

	private List<String> getEmailsToSend(Integer id, List<User> admins) {

		List<String> rootUsers = admins.stream().filter(u -> {

			boolean isOK = false;

			if (u.getRoleName().equalsIgnoreCase(Role.ROOT_USER.toString()) || u.getId().compareTo(id) == 0) {
				if (u.getEmail() != null && !u.getEmail().isEmpty())
					isOK = true;
			}
			return isOK;
		}).map(User::getEmail).collect(Collectors.toList());

		// String emails =
		// StringUtils.arrayToCommaDelimitedString(rootUsers.toArray());

		return rootUsers;
	}

	public static String getCurrentUrl(HttpServletRequest request) {
		URL url;
		try {
			url = new URL(request.getRequestURL().toString());
			String host = url.getHost();
			String userInfo = url.getUserInfo();
			String scheme = url.getProtocol();
			int port = url.getPort();
			String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
			String query = (String) request.getAttribute("javax.servlet.forward.query_string");

			URI uri = new URI(scheme, userInfo, host, port, path, query, null);
			return uri.toString();
		} catch (MalformedURLException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private User getAdminById(List<User> admins, Integer id) {

		Optional<User> result = admins.stream().filter(u -> {

			if (u.getId().compareTo(id) == 0)
				return true;
			else
				return false;

		}).findFirst();

		if (result.isPresent())
			return result.get();
		else
			return null;

	}

	private String getMessageBody(Document document) {

		// document.getOrignalName() + "is going to expired on" +
		// document.getExpireDate() + "<b> BOLD </b>"
		String template = document.getOrignalName() + " is going to expired on" + document.getExpireDate()
				+ "\n Open the following URL to stop reminder for this document \n" + getServerRootPath()
				+ "/Views/Unsecured/EmailLinks/DocReminder.xhtml?id=" + document.getId();

		// String message = String.format(template, document.getOrignalName(),
		// document.getEffectiveDate().toString(),);
		return template;
	}

	public Properties readFile(String propFileName) throws IOException {

		InputStream inputStream = null;
		Properties prop = null;
		try {
			prop = new Properties();

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return prop;
	}

	/**
	 * @param from
	 * @param to
	 * @param subject
	 * @param msg
	 */
	public void sendMail(String from, List<String> tos, String subject, String msg) {
		// creating message

		try {
			// MimeMessage message = mail.createMimeMessage();
			// message.setFrom(new Ad);

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			// message.setTo(to);

			String[] array = tos.toArray(new String[0]);

			// String[] toppings = {"hassanrazakhalid@yahoo.com",
			// "haider.khalid@sypore.com"};
			message.setTo(array);
			message.setSubject(subject);
			message.setText(msg);
			// sending message
			mail.send(message);

		} catch (Exception e) {

			logger.debug(e.toString());
			// TODO: handle exception
		}
	}

	public SharedData() {
		// final SharedData previous = INSTANCE.getAndSet(this);
		// if(previous != null)
		// throw new IllegalStateException("Second singleton " + this + "
		// created after " + previous);
		if (instance == null)
			instance = this;
		this.init();
	}

	public String getFullName() {
		String nam = getCurrentUser().getName() + " in " + getCurrentUser().getEntity().getName();
		return nam;
	}

	public static SharedData getSharedInstace() {

		/*
		 * if(instance == null) instance = new SharedData();
		 */
		return instance;
	}

	public ApplicationContext getSpringContext() {

		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(
				(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext());
		return ctx;
	}

	// Security Code
	@Transactional(readOnly = true)
	public String checkForSecurity(String userName, String password) {

		String isOK = null;

		try {
			Authentication request = new UsernamePasswordAuthenticationToken(userName, password);
			authentication = authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// HttpServletRequest httpReq =
			// (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			// HttpServletResponse httpResp =
			// (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			//
			// sessionAuthenticationStrategy.onAuthentication(authentication,
			// httpReq, httpResp);

			isOK = "correct";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			isOK = "incorrect";
			// FacesMessage message = new
			// FacesMessage(FacesMessage.SEVERITY_INFO, "Login Failed",
			// "Wrong Credincials");
			// RequestContext.getCurrentInstance().showMessageInDialog(message);
		}

		// Collection roles =
		// SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return isOK;
	}

	public boolean shouldReturnFullList() {

		boolean isOK = false;
		if (SharedData.getSharedInstace().getCurrentUser().getRoleName().equalsIgnoreCase(Role.ROOT_USER.toString())
				|| SharedData.getSharedInstace().getCurrentUser().getRoleName()
						.equalsIgnoreCase(Role.ROOT_ADMIN.toString())) {
			isOK = true;
		}

		return isOK;
	}

	// Getters Annd Setters

	public String logoutLogic() {

		SecurityContextHolder.clearContext();

		HttpSession session = session();
		if (session != null)
			session.invalidate();
		return "logout";
	}

	public static HttpSession session() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(false); // true == allow create
	}

	public User getCurrentUser() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (obj instanceof User)
			return (User) obj;
		else {
			try {
				throw new SessionExpiredException();
			}

			catch (SessionExpiredException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				return null;
			}

		}

		// ExternalContext context =
		// FacesContext.getCurrentInstance().getExternalContext();
		// try {
		//// context.getRequestContextPath() +
		// String s1 = context.getApplicationContextPath();
		// String s2 = context.getRequestContextPath();
		// String loginURL = context.getRequestContextPath()
		// +"/Views/Unsecured/Login/index.xhtml";
		// context.redirect(loginURL);
		// } catch (IOException e) {
		// // TODO Auto-generated caRtch block
		// e.printStackTrace();
		// }

		// return null;

	}

	public static String getInString(List<Integer> ids) {

		String str = "in (";

		for (int i = 0; i < ids.size(); i++) {

			Integer integer = ids.get(i);
			str += " :arg" + i;

			if (i != (ids.size() - 1)) {
				str += ",";
			}
		}
		str += ")";
		return str;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub

		mail = (JavaMailSenderImpl) applicationContext.getBean("mailSender");
	}

	public String toProviderReports(){ return "ReportProvider";}
	public String toTransactionReports(){ return "ReportTransaction";}
	public String toPracticeReports(){ return "ReportPractice";}
	public String toTransactionView(){return "ViewTransaction";}
	
	public String toCreateTransaction(){return "CreateTransaction";}
	
	public String toCreatePractice() {return "CreatePractice";}
	// public void setCurrentUser(User currentUser) {
	// this.currentUser = currentUser;
	// }
}