package org.khl.chat.client;

import java.util.ArrayList;
import java.util.List;

import org.khl.chat.client.component.Grid;
import org.khl.chat.client.component.LoginPage;
import org.khl.chat.client.dto.AppData;
import org.khl.chat.client.dto.UserDto;
import org.khl.chat.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ChatClient implements EntryPoint {
	
	public  AppData appData = AppData.INSTANCE;

	public void onModuleLoad() {
		appData.checkToken();
	}
}
