package uk.ac.abdn.socialprov.server;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvTemplateMapping {

	private String collectionId, templateType;
	private ProvTemplateMappingItem[] mappings;
	public ProvTemplateMapping(){
		super();
	}
	public ProvTemplateMapping(String collectionId, String templateType, ProvTemplateMappingItem[] mappings) {
		super();
		this.collectionId = collectionId;
		this.templateType = templateType;
		this.mappings = mappings;
	}
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public ProvTemplateMappingItem[] getMappings() {
		return mappings;
	}
	public void setMappings(ProvTemplateMappingItem[] mappings) {
		this.mappings = mappings;
	}
	@Override
	public String toString() {
		return "ProvTemplateMapping [collectionId=" + collectionId + ", templateType=" + templateType + ", mappings="
				+ Arrays.toString(mappings) + "]";
	}
	
	
	
}
