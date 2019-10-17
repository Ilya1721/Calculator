import javafx.application.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javax.script.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

public class Main_UI extends Application
{
	private VBox MainLayout;
	private HBox FieldLayout;
	private HBox[] Rows;
	private Button[][] ButtonTable;
	private String[][] ButtonText;
	private TextField Field;
	private ScriptEngineManager Manager;
	private ScriptEngine Engine;
	private final double MainWidth = 400;
	private final double MainHeight = 470;
	private final double ButtonWidth = 70;
	private final double ButtonHeight = 70;
	private final int RowsCount = 6;
	private final int ButtonInRowCount = 4;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void start(Stage MyStage) 
	{
		try 
		{
			set_widgets();
			set_button_funcs();
			set_script_engine();
		}
		catch(Exception exc) 
		{
			System.out.println(exc.toString());
		}
		
		MyStage.setTitle("Calculator");
		MyStage.setScene(new Scene(MainLayout, MainWidth, MainHeight));
		MyStage.setResizable(false);
		MyStage.show();
	}
	
	private void set_widgets() 
	{		
		ButtonText = new String[RowsCount][ButtonInRowCount];
		ButtonText[0] = new String[] {"(", ")", "%", "AC"};
		ButtonText[1] = new String[] {"7", "8", "9", "/"};
		ButtonText[2] = new String[] {"4", "5", "6", "*"};
		ButtonText[3] = new String[] {"1", "2", "3", "-"};
		ButtonText[4] = new String[] {"0", ".", "=", "+"};
		ButtonText[5] = new String[] {"sin(", "cos(", "tan(", "pow("};
			
		ButtonTable = new Button[RowsCount][ButtonInRowCount];
		for(int i = 0; i < RowsCount; ++i) 
		{
			for(int j = 0; j < ButtonInRowCount; ++j) 
			{
				ButtonTable[i][j] = new Button();
				ButtonTable[i][j].setText(ButtonText[i][j]);
				ButtonTable[i][j].setMinSize(ButtonWidth, ButtonHeight);
			}
		}

		Rows = new HBox[RowsCount];
		for(int i = 0; i < RowsCount; ++i)
		{
			Rows[i]= new HBox();
			Rows[i].setAlignment(Pos.TOP_CENTER);
			Rows[i].getChildren().addAll(ButtonTable[i]);
		}
		
		Field = new TextField();
		Field.setEditable(false);
		Field.setMinWidth(MainWidth - MainWidth * 0.3);
		Field.setMinHeight(50);
		
		FieldLayout = new HBox();
		FieldLayout.setAlignment(Pos.TOP_CENTER);
		FieldLayout.getChildren().add(Field);
		
		MainLayout = new VBox();
		MainLayout.getChildren().add(FieldLayout);
		MainLayout.getChildren().addAll(Rows);
	}
	
	private void set_button_funcs() 
	{
		for(int i = 0; i < RowsCount; ++i) 
		{
			for(int j = 0; j < ButtonInRowCount; ++j) 
			{
				final Button ButtonPtr = ButtonTable[i][j];
				ButtonTable[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() 
				{
					public void handle(MouseEvent event) 
					{
						Field.setText(Field.getText() + ButtonPtr.getText());
					}
				});
			}
		}
		for(int j = 0; j < ButtonInRowCount; ++j) 
		{
			final Button ButtonPtr = ButtonTable[5][j];
			ButtonTable[5][j].setOnMouseClicked(new EventHandler<MouseEvent>() 
			{
				public void handle(MouseEvent event) 
				{
					Field.setText(Field.getText() + "Math." + ButtonPtr.getText());
				}
			});
		}
		ButtonTable[0][3].setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent event) 
			{
				if(event.getButton() == MouseButton.SECONDARY) 
				{
					Field.setText("");
				}
				else if(event.getButton() == MouseButton.PRIMARY)
				{
					int length = Field.getText().length();
					if(length != 0) 
					{
						Field.setText(Field.getText(0, length - 1));
					}
				}
			}
		});
		ButtonTable[4][1].setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent event) 
			{
				if(event.getButton() == MouseButton.PRIMARY) 
				{
					Field.setText(Field.getText() + ".");
				}
				else if(event.getButton() == MouseButton.SECONDARY) 
				{
					Field.setText(Field.getText() + ",");
				}
			}
		});
		ButtonTable[4][2].setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent event) 
			{
				calculate();
			}
		});
	}
	
	private void calculate() 
	{
		try 
		{
			Field.setText(Engine.eval(Field.getText()).toString());
		}
		catch(Exception exc) 
		{
			System.out.println(exc.toString());
		}
	}
	
	private void set_script_engine() 
	{
		Manager = new ScriptEngineManager();
		Engine = Manager.getEngineByName("JavaScript");
	}
}
