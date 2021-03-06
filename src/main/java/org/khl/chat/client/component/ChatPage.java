package org.khl.chat.client.component;

import java.util.ArrayList;

import org.khl.chat.client.dto.AppData;
import org.khl.chat.client.dto.ChatDto;
import org.khl.chat.client.utils.MyRequestCallback;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ChatPage extends Composite {
	
	private static final String URL_GET_CHATS = "http://127.0.0.1:8080/user/${id}/chats";
	ListDataProvider<ChatDto> dataProvider = new ListDataProvider<>();
	private ArrayList<ChatDto> chats = new ArrayList<>();
	
	interface ChatPageUiBinder extends UiBinder<HTMLPanel, ChatPage> {}
	
	private static ChatPageUiBinder ourUiBinder = GWT.create(ChatPageUiBinder.class);
	private Widget widget = ourUiBinder.createAndBindUi(this);
	
	@UiField
	ShowMorePagerPanel pagerPanel;
	
	@UiField
	RangeLabelPager rangeLabelPager;
	
//	@UiField
//	MessagesForm messagesForm;

	@UiField 
	Label lable1;
	
	private CellList<ChatDto> cellList;
	
	static class ChatCell extends AbstractCell<ChatDto> {
		
		@Override
		public void render(Context context, ChatDto value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			
			sb.appendHtmlConstant("<table>");
			sb.appendHtmlConstant("<td style='font-size:95%;'>");
			sb.appendEscaped(value.getName());
			sb.appendHtmlConstant("</td></tr><tr><td>");
			sb.appendEscaped("Автор: " + value.getAuthor().getName());
			sb.appendHtmlConstant("</td></tr></table>");
		}
	}
	
	public ChatPage() {
		
		initWidget(widget);
		
		getChatsList();
		
		ChatCell contactCell = new ChatCell();
		
		cellList = new CellList<ChatDto>(contactCell,ChatDto.KEY_PROVIDER);
		cellList.setPageSize(30);
		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		
		final SingleSelectionModel<ChatDto> selectionModel = new SingleSelectionModel<ChatDto>(ChatDto.KEY_PROVIDER);
		cellList.setSelectionModel(selectionModel);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
//				messagesForm.initMsgForm(selectionModel.getSelectedObject().getId());
				lable1.setText(selectionModel.getSelectedObject().getName());
			}
		});
		
		dataProvider.addDataDisplay(cellList);
		pagerPanel.setDisplay(cellList);
		rangeLabelPager.setDisplay(cellList);
		
	}
	
	private void getChatsList() {
		String url = URL_GET_CHATS.replace("${id}", AppData.INSTANCE.getUserDto().getId().toString());
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Authorization", AppData.INSTANCE.getToken());
		try {
			builder.setCallback( new MyRequestCallback() {
				public void onError(Request request, Throwable exception) {
					Window.alert(exception.getMessage());
				}
				public void onOk(Request request, Response response) {
					
					if (201 == response.getStatusCode()){
						JSONValue jval = JSONParser.parseStrict(response.getText());
						JSONArray jarray = jval.isArray();
						chats = jsonArrayToList(jarray);
						dataProvider.setList(chats);
					} 
					else {
						Window.alert("Что-то пошло не так: " + response.getStatusText());
					}
				}
			});
			builder.send();
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}  
	
	private ArrayList<ChatDto> jsonArrayToList(JSONArray jarray) {
		ArrayList<ChatDto> list = new ArrayList<ChatDto>();
		int lenght = jarray.size();
		for (int i = 0; i < lenght; i++) {
			list.add(ChatDto.fromJson(jarray.get(i).isObject()));
		}
		return list;
	}
}
