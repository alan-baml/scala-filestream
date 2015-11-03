package sss.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;


@SuppressWarnings("serial")
@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = RugbyWorldCupUI.class)
public class Servlet extends VaadinServlet {
}

