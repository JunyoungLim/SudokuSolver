package ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sudoku.Solver;

/**
 * MainGUI.java
 * Graphic User Interface.
 */
public class MainGUI extends Application
{
	private static final String BLACK_BOX = "-fx-background-color: beige; -fx-font: 20 arial;" +
			"-fx-font-weight: bold; -fx-text-fill: black;";
	private static final String GREEN_BOX = "-fx-background-color: honeydew; -fx-font: 20 arial;" +
			"-fx-font-weight: bold; -fx-text-fill: black;";
	
	TextField[][] puzzle;
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage)
	{
		stage.setTitle("Sudoku Solver");
		stage.setResizable(true);
		
		puzzle = new TextField[9][9];
		
		VBox vBox = new VBox();
		for (int r = 0; r < 9; r++)
		{
			HBox hBox = new HBox();
			for (int c = 0; c < 9; c++)
			{
				TextField t = new TextField();
				t.setAlignment(Pos.CENTER);
				t.setStyle(BLACK_BOX);
				int index = r * 9 + c + 1;
				t.setOnKeyReleased(event ->
				{
					String text = t.getText();
					
					if (text.length() > 1)
						t.setText(String.valueOf(text.charAt(text.length() - 1)));
					else if (text.length() == 0)
						return;
					
					if (!Character.isDigit(t.getText().charAt(0)))
						t.setText("");
					else
					{
						int row = index > 80 ? 0 : index / 9;
						int col = index > 80 ? 0 : index % 9;
						puzzle[row][col].requestFocus();
					}
				});
				puzzle[r][c] = t;
				t.setPrefSize(50, 50);
				t.setBorder(new Border(new BorderStroke(Color.BLACK,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				hBox.getChildren().add(t);
			}
			vBox.getChildren().add(hBox);
		}
		Button solve = new Button("Solve");
		HBox hBox = new HBox();
		hBox.getChildren().add(solve);
		solve.setOnMouseClicked(event ->
		{
			int[][] sudoku = new int[9][9];
			boolean[][] prefill = new boolean[9][9];
			for (int r = 0; r < 9; r++)
			{
				for (int c = 0; c < 9; c++)
				{
					String text = puzzle[r][c].getText();
					if (text.isEmpty() || Integer.parseInt(text) == 0)
						sudoku[r][c] = 0;
					else
					{
						sudoku[r][c] = Integer.parseInt(text);
						prefill[r][c] = true;
					}
				}
			}
			
			if (!Solver.isValidPuzzle(sudoku))
			{
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Invalid Puzzle");
				alert.setHeaderText("The Sudoku puzzle is invalid.");
				alert.setContentText("Please input a valid Sudoku puzzle.");
				alert.showAndWait();
				return;
			}
			
			boolean successful = Solver.solve(sudoku);
			if (!successful)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Solve Failed");
				alert.setHeaderText("Unable to solve the Sudoku puzzle.");
				alert.setContentText("Please input a valid Sudoku puzzle.");
				alert.showAndWait();
				return;
			}
			
			for (int r = 0; r < 9; r++)
			{
				for (int c = 0; c < 9; c++)
				{
					if (!prefill[r][c])
						puzzle[r][c].setStyle(GREEN_BOX);
					else puzzle[r][c].setStyle(BLACK_BOX);
					puzzle[r][c].setText(String.valueOf(sudoku[r][c]));
				}
			}
		});
		Button clear = new Button("Clear");
		hBox.getChildren().add(clear);
		vBox.getChildren().add(hBox);
		clear.setOnMouseClicked(event ->
		{
			for (int r = 0; r < 9; r++)
			{
				for (int c = 0; c < 9; c++)
				{
					puzzle[r][c].setText("");
					puzzle[r][c].setStyle(BLACK_BOX);
				}
			}
		});
		
		stage.setScene(new Scene(vBox));
		
		stage.sizeToScene();
		stage.show();
	}
}
