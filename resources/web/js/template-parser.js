(function(window) {
	// I recommend this
	'use strict';
	function define_library() {
		var Library = {};
		var name = "socialprov";
		Library.greet = function() {
			alert("Hello from the " + name + " library.");
		};
		/**
		 * Copies the value/values in source to the target
		 * 
		 * @param source
		 *            the value/array of values to be added to target
		 * @param {Array}
		 *            target Where the values will be added to
		 * @return target
		 */
		Library.copyToArray = function(source, target) {
			if (source == null)
				return target;
			if ($.isArray(source)) {
				$(source).each(function(i) {
					target.push(source[i])
				});
			} else
				target.push(source);
			return target;
		}
		// gets the object with @id:resourceId from the @graph
		Library.get = function(graph, resourceId) {
			// console.log("getting " + elementId)
			var item = null;
			$(graph).each(function(i) {
				if (graph[i]["@id"] == resourceId) {
					item = graph[i];
					return;
				}
			});
			// JSON-LD appears to loose the prefix on properties when describing
			// a resource, so if haven't found
			// the resource (which is a property) then do a text search on the
			// names
			if (item == null) {
				$(graph).each(function(i) {
					if (graph[i]["@id"].endsWith(resourceId)) {
						item = graph[i];
						return;
					}
				});
			}
//			console.log("getting " + resourceId + " and found " + item)
//			console.log(item)
			return item;
		};
		Library.template = {};
		// Builds the HTML "form" for acquiring data for the variable with id
		// variableId
		// template - the entire JSON-LD doc
		// variableId - the @id of the variable that the template will be
		// elementId = the id of the div element that this template display will be added to
		// created for
		Library.buildGui = function(template, variableId, elementId, labelNeeded) {
			if (template["@graph"] == null){
				return;
			}
			Library.template = template;
			// first build the dataset template
			var datasetTemplate = Library.get(template["@graph"], variableId);
			var templateLabel = Library.getLabelFor(template["@graph"], datasetTemplate["@id"]);
			var html = "";
			if (labelNeeded = true && templateLabel != null){
				html = "<div class=\"row\" id=\"prompt-" + elementId + "-" + variableId + "-label\">" +
				"<div class=\"prompt-query col-sm-12\" id=\"query-" + elementId + "-" + variableId + "-label\" data-prop=\"label\" data-var=\"" + variableId + "\">" + templateLabel
				+ " <button id=\"btn-" + variableId + "-label\" onclick=\"selectLabelFor('" + variableId + "\', this, '"+elementId+"');\" class=\"btn btn-default\">Add</button>" +
						"</div>" +
						"</div>";
			}
			$.each(datasetTemplate, function(key, value) {
				console.log("building interface element for " + key + "," + value);
				if (key != "@id" && key != "@type" && key != "skos:prefLabel") {
					
				//	Library.getValueTypes(template['@graph'], Library.getType(datasetTemplate), key);
					var label = Library.getLabelFor(template["@graph"], key);
					// this is a horrible hack, but if there is no label then see if the value has a JSON object of its own and
					// if so add those elements
					if (label == null){
						html += Library.buildGui(template, value, elementId);
					} else {
						html += "<div class=\"row\" id=\"prompt-" + elementId + "-" + key +"\">" +
							"<div class=\"prompt-query col-sm-12\" id=\"query-" + elementId + "-" + key  + "\" data-prop=\"" + key + "\" data-var=\"" + value + "\">" + label
							+ " <button id=\"btn-"+elementId + "-" + key + "\"onclick=\"selectEntityFor('" + variableId + "\',\'" + key + "','" + value + "', this, '"+elementId+"');\" class=\"btn btn-default\">Add</button><button class=\"btn btn-default\" id=\"btn-"+elementId + "-" + key + "-reminder\" onclick=\"addReminder('" + variableId + "\',\'" + key + "','" + value + "', this, '"+elementId+"')\">Remind me later</button>" +
									"</div>" +
									"</div>";
					}
				}
			});
			return html;
		};

		Library.getLabelFor = function(graph, resourceId) {
//			 console.log("getting label for " + resourceId);
			var resource = Library.get(graph, resourceId);
			if (resource != null && resource["skos:prefLabel"]) {
				return resource["skos:prefLabel"];
			}
			return null;
		};

		// returns an array of the types 
		Library.getFullUri = function(template, types){
			var fullUris = [];
			$(types).each(function(i){
				var type = types[i];
				var i = type.indexOf(":");
				if (i > -1){
					var prefix = type.substring(0, i);
					fullUris.push(template['@context'][prefix] + type.substring(i+1))
				} else {
					// naive approach, which will have issues but for the
					// prototype we'll go with it
					fullUris.push(type)
				}
			});
			return fullUris;
		};
		
		// gets the @type for the item with @id resourceId
		Library.getTypeForResource = function(template, resourceId){
			return Library.getType(Library.get(template['@graph'], resourceId));
		};
		
		// gets the @type for the item
		Library.getType = function(item) {
			var type = [];
			// console.log("getting type for " + JSON.stringify(item));
			$.each(item, function(key, value) {
				if (key == "@type") {
					Library.copyToArray(value, type);
					return;
				}
			});
			return type;
		};
		// gets the permitted types for a property in the domain of a set of
		// classes
		// graph - the graph object from the JSON
		// types - array of the id/uri/names of the class(es) that the template
		// is for (i.e. the intended types of the variable the we are building
		// the GUI for)
		// property - the property that we are interested in
		Library.getValueTypes = function(graph, types, property) {
			console.log("getting values types for " + types + " " + property)
			var valueTypes = [];
			// first try to see if there is a range restriction
			var prop = Library.get(graph, property);
			if (prop != null && prop['range'] != null) {
				valueTypes = Library.copyToArray(prop['range'], valueTypes);
			}
			console.log("range of " + property + " is " + valueTypes);

			// then try to see if there are allValuesFrom or someValuesFrom
			// restrictions

			// change types to an array if necessary
			if (!($.isArray(types))) {
				var t = types;
				types = [ t ];
			}

			$(types).each(function(i) {
				// get the object that has the id of the type
				// (i.e. the class)
				var clsDef = Library.get(graph, types[i]);
				if (clsDef != null && clsDef['subClassOf'] != null) {
					var superClses = clsDef['subClassOf'];
					if (!($.isArray(superClses))) {
						var s = superClses;
						superClses = [ s ];
					}
					$(superClses).each(function(i) {
						var superCls = Library.get(graph, superClses[i]);
						if (superCls['@type'] == "owl:Restriction") {
							var prop = superCls['onProperty'];
							if (superCls['onProperty'] == property || superCls['onProperty'].substring(superCls['onProperty'].indexOf(":")+1) == property) {
								valueTypes = Library.copyToArray(superCls['allValuesFrom'], valueTypes);
								valueTypes = Library.copyToArray(superCls['someValuesFrom'], valueTypes);
							}
						}
					});
				}
			});
			console.log("full range is " + valueTypes)
			return valueTypes;
		};
		Library.process = function(template, div) {
			// console.log(JSON.stringify(template["@graph"]))
			var html = "";
			// iterating through the @graph, looking at each resource
			$(template["@graph"]).each(function(i) {
				html += "<br/>";
				var tripleGroup = template["@graph"][i];

				// handle the @id and @type of this resources
				html += Library.getDisplayValueFor(tripleGroup["@id"]);
				// if (tripleGroup["@id"].startsWith("var:")) {
				// html += "SOME ID for " + tripleGroup["@id"] +
				// " ";
				// }
				if ($.isArray(tripleGroup["@type"])) {
					html += " is a ";
					$(tripleGroup["@type"]).each(function(typeI) {
						html += Library.getDisplayValueFor(tripleGroup["@type"][typeI]) + " ";
					});
				} else {
					html += " is a " + Library.getDisplayValueFor(tripleGroup["@type"]);
				}

				// handle the remaining properties
				for ( var key in tripleGroup) {
					// ignore the id and type
					if (key != "@id" && key != "@type") {
						html += key + " " + Library.getDisplayValueFor(tripleGroup[key]) + " ";
					}
				}
				;

			});
			$(div).html(html);
		};
		// what does this actually do? How does it differ from getLabelFor?
		Library.getDisplayValueFor = function(item, graph) {
			if (item.startsWith("var:")) {
				return "(some id for " + item + ")";
			} else {
				// find prefLabel if there is one
				$(graph).each(function(i) {
					var item = graph[i];
					if (item["@id"] == item) {
						if (item["skos:prefLabel"] != null) {
							// console.log("preflabel for " + item
							// + " is " + item['skos:prefLabel'])
						}
					}
				});
				return item;
			}
		};
		
		// gets all of the properties associated with the resource with @id == variableId
		Library.getProperties = function(template, variableId){
			var properties = [];
			var datasetTemplate = Library.get(template["@graph"], variableId);
			$.each(datasetTemplate, function(key, value) {
                if (key == "skos:prefLabel"){
                    properties.push({"property":"skos:prefLabel", "label":Library.getLabelFor(template["@graph"], datasetTemplate["@id"])})
                } else if (key != "@id" && key != "@type" ) {
					
					var label = Library.getLabelFor(template["@graph"], key);
					// this is a horrible hack, but if there is no label then see if the value has a JSON object of its own and
					// if so add those elements
					if (label == null){
						properties = properties.concat(Library.getProperties(template, value));
					} else {
						properties.push({"property":key, "label":label})
					}
				}
			});
			return properties;
		}

		return Library;
	}
	// define globally if it doesn't already exist
	if (typeof (Library) === 'undefined') {
		window.Library = define_library();
	} else {
		console.log("Library already defined.");
	}
})(window);