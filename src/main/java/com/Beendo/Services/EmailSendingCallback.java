package com.Beendo.Services;

import java.util.List;

import com.Beendo.Entities.Document;
import com.Beendo.Entities.User;

public interface EmailSendingCallback {

	public void getEmailsData(List<Document>documents, List<User>admins);
}
