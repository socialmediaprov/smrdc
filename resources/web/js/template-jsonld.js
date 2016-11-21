{
	"@graph": [{
		"@id": "var:author",
		"@type": ["prov:Person", "prov:Entity"],
		"name": "var:name"
	}, {
		"@id": "var:quote",
		"@type": "prov:Entity",
		"value": "var:value",
		"wasAttributedTo": "var:author"
	}],
	"@context": {
		"name": {
			"@id": "http://xmlns.com/foaf/0.1/name",
			"@type": "@id"
		},
		"wasAttributedTo": {
			"@id": "http://www.w3.org/ns/prov#wasAttributedTo",
			"@type": "@id"
		},
		"value": {
			"@id": "http://www.w3.org/ns/prov#value",
			"@type": "@id"
		},
		"": "http://www.example.org/ns/test#",
		"vargen": "http://openprovenance.org/vargen#",
		"tmpl": "http://openprovenance.org/tmpl#",
		"var": "http://openprovenance.org/var#",
		"rdfs": "http://www.w3.org/2000/01/rdf-schema#",
		"prov": "http://www.w3.org/ns/prov#",
		"foaf": "http://xmlns.com/foaf/0.1/"
	}
}