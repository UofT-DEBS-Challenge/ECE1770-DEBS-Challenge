package debs.challenge.client.client;

import debs.challenge.client.shared.FieldVerifier;
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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Client implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		//Initializing Charts for query 1: 24h trend for Chem Additive Sensor A,B,C 
		// Create a callback to be called when the visualization API has been loaded.
	    
		Runnable onLoadCallback = new Runnable() {
	      public void run() {
	        Panel Panel = RootPanel.get();
	        AreaChart chart_chem1 = new AreaChart(createTable1(), createOptions1());
	        AreaChart chart_chem2 = new AreaChart(createTable2(), createOptions2());
	        AreaChart chart_chem3 = new AreaChart(createTable3(), createOptions3());
	  	    Panel.add(chart_chem1);
	  	    Panel.add(chart_chem2);
	  	    Panel.add(chart_chem3);
	      }
	    };
	    
	    VisualizationUtils.loadVisualizationApi(onLoadCallback, AreaChart.PACKAGE);
	    
		RootPanel rootPanel = RootPanel.get();
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		rootPanel.add(verticalPanel_2, 776, 196);
		verticalPanel_2.setSize("93px", "109px");
		
		TextBox textBox_13 = new TextBox();
		verticalPanel_2.add(textBox_13);
		textBox_13.setWidth("85px");
		
		TextBox textBox_14 = new TextBox();
		verticalPanel_2.add(textBox_14);
		textBox_14.setWidth("84px");
		
		TextBox textBox = new TextBox();
		verticalPanel_2.add(textBox);
		textBox.setWidth("84px");
		
		FlexTable flexTable = new FlexTable();
		rootPanel.add(flexTable, 33, -1);
		flexTable.setSize("161px", "92px");
		
		Label lblNewLabel_8 = new Label("MSRG Monitoring Tool");
		lblNewLabel_8.setStyleName("gwt-Label-MSRGMonitoringtool");
		rootPanel.add(lblNewLabel_8, 221, 10);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		rootPanel.add(verticalPanel, 687, 196);
		verticalPanel.setSize("64px", "109px");
		
		Label lblSensorA = new Label("Sensor A");
		verticalPanel.add(lblSensorA);
		lblSensorA.setSize("92px", "18px");
		
		Label lblSensorB = new Label("Sensor B");
		verticalPanel.add(lblSensorB);
		lblSensorB.setSize("92px", "18px");
		
		Label lblSensorC = new Label("Sensor C");
		verticalPanel.add(lblSensorC);
		lblSensorC.setSize("92px", "18px");
		
		Label lblAveragePowerConsumption = new Label("Average Energy Consumption");
		rootPanel.add(lblAveragePowerConsumption, 687, 170);
		lblAveragePowerConsumption.setSize("192px", "18px");
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		rootPanel.add(verticalPanel_1, 687, 361);
		verticalPanel_1.setSize("64px", "109px");
		
		Label label = new Label("Sensor A");
		verticalPanel_1.add(label);
		label.setSize("92px", "18px");
		
		Label label_1 = new Label("Sensor B");
		verticalPanel_1.add(label_1);
		label_1.setSize("92px", "18px");
		
		Label label_2 = new Label("Sensor C");
		verticalPanel_1.add(label_2);
		label_2.setSize("92px", "18px");
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		rootPanel.add(verticalPanel_3, 776, 361);
		verticalPanel_3.setSize("93px", "109px");
		
		TextBox textBox_1 = new TextBox();
		verticalPanel_3.add(textBox_1);
		textBox_1.setWidth("85px");
		
		TextBox textBox_2 = new TextBox();
		verticalPanel_3.add(textBox_2);
		textBox_2.setWidth("84px");
		
		TextBox textBox_3 = new TextBox();
		verticalPanel_3.add(textBox_3);
		textBox_3.setWidth("84px");
		
		Label lblRelativeVariationEnergy = new Label("Relative Variation of Energy Consumption");
		rootPanel.add(lblRelativeVariationEnergy, 687, 322);
		lblRelativeVariationEnergy.setSize("161px", "18px");
		
		VerticalPanel verticalPanel_4 = new VerticalPanel();
		rootPanel.add(verticalPanel_4, 687, 527);
		verticalPanel_4.setSize("64px", "109px");
		
		Label label_3 = new Label("Sensor A");
		verticalPanel_4.add(label_3);
		label_3.setSize("92px", "18px");
		
		Label label_4 = new Label("Sensor B");
		verticalPanel_4.add(label_4);
		label_4.setSize("92px", "18px");
		
		Label label_5 = new Label("Sensor C");
		verticalPanel_4.add(label_5);
		label_5.setSize("92px", "18px");
		
		VerticalPanel verticalPanel_5 = new VerticalPanel();
		rootPanel.add(verticalPanel_5, 786, 527);
		verticalPanel_5.setSize("93px", "109px");
		
		TextBox textBox_4 = new TextBox();
		verticalPanel_5.add(textBox_4);
		textBox_4.setWidth("85px");
		
		TextBox textBox_5 = new TextBox();
		verticalPanel_5.add(textBox_5);
		textBox_5.setWidth("84px");
		
		TextBox textBox_7 = new TextBox();
		verticalPanel_5.add(textBox_7);
		textBox_7.setWidth("84px");
		
		Label lblPowerConsumption = new Label("Power Consumption  during last one mintue");
		rootPanel.add(lblPowerConsumption, 686, 485);
		lblPowerConsumption.setSize("183px", "36px");
		
		Label lblTimestamp = new Label("TimeStamp");
		rootPanel.add(lblTimestamp, 899, 488);
		lblTimestamp.setSize("183px", "36px");
		
		VerticalPanel verticalPanel_6 = new VerticalPanel();
		rootPanel.add(verticalPanel_6, 898, 527);
		verticalPanel_6.setSize("100px", "109px");
		
		TextBox textBox_8 = new TextBox();
		verticalPanel_6.add(textBox_8);
		textBox_8.setSize("84px", "16px");
		
		TextBox textBox_10 = new TextBox();
		verticalPanel_6.add(textBox_10);
		textBox_10.setSize("85px", "16px");
		
		TextBox textBox_9 = new TextBox();
		verticalPanel_6.add(textBox_9);
		textBox_9.setSize("84px", "16px");
		
		Image image = new Image("images/images/alert.gif");
		image.setSize("52px", "52px");
		rootPanel.add(image, 438, 196);
		
		
		Image image_1 = new Image("images/images/alert.gif");
		image_1.setSize("52px", "52px");
		//rootPanel.add(image_1, 438, 389);
		
		
		Image image_2 = new Image("images/images/alert.gif");
		image_2.setSize("52px", "49px");
		//rootPanel.add(image_2, 438, 546);
		
		
	}
	
	private Options createOptions1() {
		// TODO Auto-generated method stub
		Options options = Options.create();
	    options.setWidth(400);
	    options.setHeight(180);
	  //  options.set3D(true);
	    options.setTitle("Chem Additive Sensor A");
	    return options;
	}
	
	private Options createOptions2() {
		// TODO Auto-generated method stub
		Options options = Options.create();
	    options.setWidth(400);
	    options.setHeight(180);
	  //  options.set3D(true);
	    options.setTitle("Chem Additive Sensor B");
	    return options;
	}

	private Options createOptions3() {
		// TODO Auto-generated method stub
		Options options = Options.create();
	    options.setWidth(400);
	    options.setHeight(180);
	  //  options.set3D(true);
	    options.setTitle("Chem Additive Sensor C");
	    return options;
	}
	    
    private AbstractDataTable createTable1() {
         //TODO Auto-generated method stub
    	DataTable data = DataTable.create();
    	data.addColumn(ColumnType.STRING, "Task");
    	data.addColumn(ColumnType.NUMBER, "Time diff. between state occurences");
    	data.addRows(2);
    	data.setValue(0, 0, "Work");
    	data.setValue(0, 1, 14);
    	data.setValue(1, 0, "Sleep");
    	data.setValue(1, 1, 10);
    	return data;
    }
    
	private AbstractDataTable createTable2() {
		// TODO Auto-generated method stub
		DataTable data = DataTable.create();
	    data.addColumn(ColumnType.STRING, "Task");
	    data.addColumn(ColumnType.NUMBER, "Time diff. between state occurences");
	    data.addRows(2);
	    data.setValue(0, 0, "Work");
	    data.setValue(0, 1, 5);
	    data.setValue(1, 0, "Sleep");
	    data.setValue(1, 1, 10);
		return data;
	}

	private AbstractDataTable createTable3() {
		// TODO Auto-generated method stub
		DataTable data = DataTable.create();
	    data.addColumn(ColumnType.STRING, "Task");
	    data.addColumn(ColumnType.NUMBER, "Time diff. between state occurences");
	    data.addRows(2);
	    data.setValue(0, 0, "Work");
	    data.setValue(0, 1, 14);
	    data.setValue(1, 0, "Sleep");
	    data.setValue(1, 1, 10);
		return data;
	}	
}