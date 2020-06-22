package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Arco;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

//controller turno A --> switchare al branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxCategoria;

    @FXML
    private ComboBox<Integer> boxAnno;

    @FXML
    private Button btnAnalisi;

    @FXML
    private ComboBox<Arco> boxArco;

    @FXML
    private Button btnPercorso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	Arco a = this.boxArco.getValue();
    	
    	
    	List<String> cammino = model.cammino(a.getS1(), a.getS2());
    	
    	this.txtResult.appendText("Cammino trovato tra: "+a.getS1()+" e "+a.getS2()+"\nPeso : "+model.pesoMin()+"\n");
    	
    	for(String s : cammino) {
    		this.txtResult.appendText(s+"\n");
    	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	String categoria= this.boxCategoria.getValue();
    	Integer anno = this.boxAnno.getValue();
    	
    	model.creaGrafo(categoria, anno);
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText(String.format("#vertici: %d\n#archi: %d \n", model.nVertici(), model.nArchi()));
    	
    	txtResult.appendText("\nElenco archi con peso pari al peso massimo:\n");
    	
    	for(Arco a : model.archiFiltro()) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	
    	this.boxArco.getItems().clear();
    	this.boxArco.getItems().addAll(model.archiFiltro());
    	this.boxArco.setValue(model.archiFiltro().get(0));
    	
    	

    }

    @FXML
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxCategoria.getItems().addAll(model.getCategorie());
		this.boxCategoria.setValue(model.getCategorie().get(0));
		
		this.boxAnno.getItems().addAll(model.listaAnni());
		this.boxAnno.setValue(model.listaAnni().get(0));
	}
}
