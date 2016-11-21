package uk.ac.abdn.socialprov.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvTemplateMappingItem {

	private String property, valueLabel, variable, subjectVariable, selectedValue;

	public ProvTemplateMappingItem(){
		super();
	}
	
	public ProvTemplateMappingItem(String property, String value, String variable, String subjectVariable,
			String selectedValue) {
		super();
		this.property = property;
		this.valueLabel = value;
		this.variable = variable;
		this.subjectVariable = subjectVariable;
		this.selectedValue = selectedValue;
	}

	public String getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(String valueLabel) {
		this.valueLabel = valueLabel;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return valueLabel;
	}

	public void setValue(String value) {
		this.valueLabel = value;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getSubjectVariable() {
		return subjectVariable;
	}

	public void setSubjectVariable(String subjectVariable) {
		this.subjectVariable = subjectVariable;
	}


	@Override
	public String toString() {
		return "ProvTemplateMapping [property=" + property + ", valueLabel=" + valueLabel + ", variable=" + variable
				+ ", subjectVariable=" + subjectVariable + ", selectedValue=" + selectedValue + "]";
	}
}
