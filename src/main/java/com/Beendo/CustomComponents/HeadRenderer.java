package com.Beendo.CustomComponents;

import java.io.IOException;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

@ResourceDependencies({
    @ResourceDependency(library="primefaces-bootstrap", name="theme.css"),
    @ResourceDependency(library="primefaces", name="primefaces.js"), // Only necessary when at least one validation JS files needs to be included.
    @ResourceDependency(library="primefaces", name="validation/validation.js"), // Only necessary when you need <p:clientValidator>.
    @ResourceDependency(library="primefaces", name="validation/beanvalidation.js") // Only necessary when you use JSR303 bean validation.
})
public class HeadRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        context.getResponseWriter().startElement("head", component);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        // NOOP.
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        for (UIComponent resource : context.getViewRoot().getComponentResources(context, "head")) {
            resource.encodeAll(context);
        }

        context.getResponseWriter().endElement("head");
    }

}