		var type;
		var currentPromptSelection = {};
		//var mappings = [];
		var mappingData = {};
		var selectedValueFunction, datePicker;
		$(document)
				.ready(
						function() {
							$("#controls").hide();

							$('#btn-add-dataset').click(
									function() {
										displayTemplate(
												"#prompts-datasets-content",
												"dbr:dataset",
												window.Library.buildGui,
												"vargen:rawDatasetId");
									});
							$('#btn-add-prep').click(
									function(){
										displayTemplate("#prompts-dataprep-content",
												"prism:DataPreparationActivity",
												window.Library.buildGui, 
												"vargen:dataprepActivityId");	
									});
							$('#btn-add-analysis').click(
									function(){
									displayTemplate("#prompts-dataanalysis-content",
											"prism:DataAnalysisActivity",
											window.Library.buildGui, 
											"vargen:dataAnalysisActivityId");	
									})
									
							$('#btn-create-report').click(
									function(){
										generateReportCard();
									});	
							
							$('#btn-create-prov')
									.click(
											function() {
												var mappingsToSend = [];
												$.each(mappingData,
																function(key) {
																	var obj = {'collectionId':key, 'templateType':mappingData[key]['templateType'], mappings:[]};
																	var mappings = mappingData[key]['mappings'];
																	$.each(mappings, function(i){
																		var m = {};
																		m['property'] = mappings[i]['property']
																		m['valueLabel'] = mappings[i]['valueLabel'];
																		m['variable'] = mappings[i]['variable'];
																		m['subjectVariable'] = mappings[i]['subjectVariable'];
																		m['selectedValue'] = mappings[i]['selectedValue'];
																		obj['mappings']
																				.push(m);
																	});
																	mappingsToSend.push(obj);
																});
												console.log(JSON
																	.stringify(mappingsToSend))
												var jqxhr = $
														.ajax({
															url : "http://localhost:8080/generateProv",
															data : JSON
																	.stringify(mappingsToSend),
															contentType : "application/json",
															type : 'POST',
															//dataType:'jsonp',
															success : function(
																	data) {
																console
																		.log(data);
															},
															error : function(
																	XMLHttpRequest,
																	textStatus,
																	errorThrown) {
																alert("failed");
																console
																		.log(textStatus);
																console
																		.log(errorThrown)
															}
														});
											});

							$("#controls-btn-save").click(function(){
								saveEntity();
							});
							
							// setup controls for link panel
							$('.linktype').click(
									function() {
										console.log(type);
										type = $(this).attr("value");
										$(".linktype").attr('class',
												"btn btn-default linktype");
										$(this).attr('class',
												"btn btn-primary linktype");
										console.log(type);

									});
                            
                            $('#accordion').on('hide.bs.collapse', function () {
                                var closedPanel = $('#accordion .in');
                                var content = closedPanel.data("variable");
                                var header = closedPanel.attr("aria-labelledby");
                                var summary = generateHeaderSummary(content);
                                if (summary['numberQuestions'] > 0){
                                    $("#"+header+"-summary").html(" - " + summary["numberResponses"] + " of " + summary["numberQuestions"] + " questions answered");
                                }
                            })

						});
		var templates = {};
		var getTemplateForGuiURL = "http://localhost:8080/getTemplateForGui";
		// if insertAfter == true, then the new template is added the elementToAddTo and no remove button is added
		function displayTemplate(elementToAddTo, type, successCallback,
				variable) {
		
			var jqxhr = $.ajax({
				url : getTemplateForGuiURL,
				data : {
					'type' : type
				},
				type : 'GET',
				//dataType:'jsonp',
				success : function(data) {
					var template = JSON.parse(data);
					var id = guid();
					var r = successCallback(template, variable, id, true);
					templates[id] = template;
					var div = "<div class='template-display' id='"+id+"'><div class='row'><button class='btn btn-default btn-remove' onclick=\"removeTemplateMappings('"+id+"');\">Remove</button></div>" + r + "</div>";
					$(elementToAddTo).append(div);
					mappingData[id] = {'ui-id':id, 'templateType':window.Library.getFullUri(template,[type])[0],'variable':variable, 'mappings':[]};
					console.log(mappingData);
					return r;
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("failed");
					console.log(textStatus);
					console.log(errorThrown)
				}
			});
		}
		/**
		 * Removes the UI elements for the div with id elementId, and also any entry in the data variable associated with
		 * that id
		 */
		function removeTemplateMappings(elementId){
			// TODO remove any text highlights
			
			console.log("removing template and mappings" + elementId);
			 delete templates[elementId]
			 delete mappingData[elementId];
			 $("#"+elementId).remove();
			 console.log(mappingData);
		 }
		
		/**
		 * Called when user clicks to add a label to a dataset, data prep option, etc
		 * variableId - the id of the dataset etc in the template (e.g. vargen:rawDatasetId))
		 */
		 function selectLabelFor(variableId, buttonElement, templateElementId){
    		resetCurrentPromptSelection()
			currentPromptSelection = {};
			currentPromptSelection['subjectVariable'] = variableId;
			currentPromptSelection['property'] = "label";
			currentPromptSelection['variable'] = "label";
			currentPromptSelection['element'] = buttonElement;
			currentPromptSelection['elementId'] = templateElementId;
			$(buttonElement).removeClass("btn-default");
			$(buttonElement).addClass("btn-info");
 			updateResourceSelectionPanel("label");
		 }
		
    /**
     * Called when user clicks to be reminded later of a question
     * subjectVariable - the resource that the value of a property is being provided
     * property - the property of subjectVariable that a value is being provided for
     * variable - the variable currently acting as the value for that property
     * buttonElement - the button that triggered the event
     * templateElementId - the template id in templates (and so the id in mappingData and of the HTML element) that subjectVariable is being assigned a value for
     */
    function addReminder(subjectVariable, property, variable, buttonElement, templateElementId){
        // just to be safe
        resetCurrentPromptSelection();
        // disable the buttons
        $("#btn-"+templateElementId +"-"+property).prop("disabled", true);
        $("#btn-"+templateElementId +"-"+property+"-reminder").prop("disabled", true);
        var id = guid(); // an id for this reminder
			
        // we don't have values for currentPromptSelection['valueLabel'] or currentPromptSelection['selectedValue'] = selectedValue;
		// so just note that its a reminder
        resetCurrentPromptSelection()
		currentPromptSelection = {};
		currentPromptSelection['subjectVariable'] = subjectVariable;
		currentPromptSelection['property'] = property;
		currentPromptSelection['variable'] = variable;
		currentPromptSelection['element'] = buttonElement;
		currentPromptSelection['elementId'] = templateElementId;
        currentPromptSelection['reminder'] = true;
		currentPromptSelection['id'] = id;
		mappingData[templateElementId]['mappings'].push(currentPromptSelection)
    }

		/**
		 * Called when user clicks to add a value to a prompt question
		 */
		function selectEntityFor(subjectVariable, property, variable, buttonElement, templateElementId) {
			resetCurrentPromptSelection()
			currentPromptSelection = {};
			currentPromptSelection['subjectVariable'] = subjectVariable;
			currentPromptSelection['property'] = property;
			currentPromptSelection['variable'] = variable;
			currentPromptSelection['element'] = buttonElement;
			currentPromptSelection['elementId'] = templateElementId;
			console.log(currentPromptSelection)
			$(buttonElement).removeClass("btn-default");
			$(buttonElement).addClass("btn-info");

			var template = templates[templateElementId];
			console.log(Library.getTypeForResource(template, subjectVariable));
			var appropriateTypes = window.Library.getValueTypes(
					template['@graph'], Library.getTypeForResource(template,
							subjectVariable), property);
			var dereferencedTypes = window.Library.getFullUri(template,
					appropriateTypes);
			if (dereferencedTypes.length > 0) {
				updateResourceSelectionPanel(dereferencedTypes[0]);
			} else {
				updateResourceSelectionPanel("http://www.w3.org/2002/07/owl#Thing");
			}
		}

		function getAnotationConceptsFromOntology(type) {
			return updateResourceSelectionPanel(type);
		}

		var controlDatePickerElement = $("#controls-options-datepicker");
		var controlStringElement = $("#controls-options-string");
		var controlNumberElement = $("#controls-options-number");
		
		function updateResourceSelectionPanel(type) {
			console.log("updating resource selection for " + type);
			$("#controls-options").empty();
			if ('label' == type){
				$("#controls-msg").html("Please select the value from the text.");
				$("#controls").show();
				selectedValueFunction = getTextAreaSelection;
			} else if ("http://www.w3.org/2001/XMLSchema#string" == type) {
				$("#controls-msg").html("Please select the information in the text, or select a pointer to where that information can be found.");
//				$("#controls-options").append(controlStringElement);

				selectedValueFunction = getTextAreaSelection;
				$("#controls").show();
				selectedValueFunction = function(){return $("#controls-options-string-input").val()}
			} else if ("http://www.w3.org/2001/XMLSchema#integer" == type) {
				$("#controls-msg").html("Please enter the value below.");
				$("#controls-options").append(controlNumberElement);
				$("#controls").show();
				selectedValueFunction = function(){return $("#controls-options-number-input").val()}
			} else if ("http://www.w3.org/2001/XMLSchema#double" == type) {
				$("#controls-msg").html("Please enter the value below.");
				$("#controls-options").append(controlNumberElement);
				$("#controls").show();
				selectedValueFunction = function(){return $("#controls-options-number-input").val()}
			} else if ("http://www.w3.org/2001/XMLSchema#date" == type) {
				$("#controls-msg").html("Please specify the date.");
				datePicker = controlDatePickerElement.datetimepicker({
	                viewMode: 'years',
	                format:'DD MMM YYYY'
	            });
				selectedValueFunction = function(){return datePicker.data("DateTimePicker").date().utc().format("YYYY-MM-DD")};
				$("#controls-options").append(controlDatePickerElement);
	            $("#controls").show();
			} else if ("http://www.w3.org/2001/XMLSchema#time" == type) {
				$("#controls-msg").html("Please specify the time.");
				datePicker = controlDatePickerElement.datetimepicker({
	                viewMode: 'years',
	                format:'mm:HH'
	            });
				selectedValueFunction = function(){return datePicker.data("DateTimePicker").date().utc().format("hh:mm:ssZ")};
				$("#controls-options").append(controlDatePickerElement);
	            $("#controls").show();
			} else if ("http://www.w3.org/2001/XMLSchema#dateTime" == type) {
				$("#controls-msg").html("Please specify the date and time.");
				datePicker = controlDatePickerElement.datetimepicker({
                    viewMode: 'years',
                    format:'DD MMM YYYY HH:mm'
                });
				selectedValueFunction = function(){return datePicker.data("DateTimePicker").date().utc().format("YYYY-MM-DDThh:mm:ssZ");};
				$("#controls-options").append(controlDatePickerElement);
                $("#controls").show();
			} else  {
				console.log(mappingData);
				// Add any items of this type that have already been defined
				var list = "";
				$.each(mappingData, function(key, value){
					console.log(type)
					console.log(value["templateType"])
					if (value['templateType'] == type){
						$.each(value['mappings'], function(index){
							if (value['mappings'][index]['property'] == "label"){
								list += "<div class='radio'><label><input type='radio' name='resource-individual-selected' value='"+key+"' onclick=\"currentPromptSelection[\'selectedValue\'] = '"+value['mappings'][index]['selectedValue']['string']+"'\"> "
								+ value['mappings'][index]['selectedValue']['string']
								+ "</label></div>";
							}
						});
					}
				});
				if (list!=""){
					var form = "<form><div class='resource-individual-select-options'>"
						+ list + "</div></form>"
				selectedValueFunction = function(){return currentPromptSelection['selectedValue']};
				$("#controls-msg").html("Please select the option from the list below")
				$("#controls-options").html(form);
				$("#controls").show();
				return;					
				};
				// query the server to retrieve more options
				
				var url = "http://localhost:8080/getClassesAndIndividuals"
				currentPromptSelection['selectedValue'] = type;
				//			var url = "http://sj.abdn.ac.uk/SocialProv/annotationlabels.php";
				//			console.log(encodeURIComponent(type))
				//			var url = "http://localhost:4567/resources/id=" + encodeURIComponent(type);
				var jqxhr = $
						.ajax({
							url : url,
							data : {
								'type' : type
							},
							type : 'GET',
							//dataType:'jsonp',
							success : function(data) {
								var d = JSON.parse(data);
								var buttons = "", list = "";
								$(d.results.bindings)
										.each(
												function(i) {
													var result = d.results.bindings[i];
													if (result.class) {
														buttons += "<button type='button' class='btn bt-secondary onclick='updateResouceSelectionPanel('"+result.class.value+"');'>"
																+ result.label.value
																+ "</button>";
													} else if (result.individual) {
														list += "<div class='radio'><label><input type='radio' name='resource-individual-selected' value='"+result.individual.value+"' onclick=\"currentPromptSelection[\'selectedValue\'] = '"+result.individual.value+"'\"> "
																+ result.label.value
																+ "</label></div>";
													}
												})
								var form = "<form><div>"+ buttons+ "</div>";
								if (list!=""){
									form += "<div class='resource-individual-select-options'>"
										+ list + "</div></form>"
								};
								selectedValueFunction = function(){return currentPromptSelection['selectedValue']};
								$("#controls-msg").html("Please select the option from the list below")
								$("#controls-options").html(form);
								$("#controls").show();
							},
							error : function(XMLHttpRequest, textStatus,
									errorThrown) {
								//alert("failed");
								console.log(textStatus);
								console.log(errorThrown)
							}
						});
			}
		}

		function saveCurrentHighlightedConceptTypeToURIVar(URI) {
			currentPromptSelection['selectedValue'] = URI;
		}

		function resetCurrentPromptSelection() {
			$(currentPromptSelection['element']).addClass("btn-default");
			$(currentPromptSelection['element']).removeClass("btn-info");
			currentPromptSelection = {};
			$("#controls").hide();
//            if (datePicker != null) datePicker.clearDates();
            $("#controls-options-string-input").val("");;
		    $("#controls-options-number-input").val("");;
		}

		function select(id) {
			$("#controls").show();
		}

		function saveEntity() {
			
			
			// validate that some text has been selected
			var textSelection = getTextAreaSelection();
			if ("" == textSelection.string || null == textSelection.string) {
				alert("Please select some text");
				return;
			}
			
			var selectedValue = selectedValueFunction();
			if (selectedValue == null){
				alert("Please select a value");
				return;
			}
			
			var id = guid(); // an id for this mapping
			highlightText(textSelection, id+"-text-highlight")
			
			// store it
			$("#platform").html(currentPromptSelection);
			currentPromptSelection['valueLabel'] = textSelection;
			currentPromptSelection['selectedValue'] = selectedValue;
			
			currentPromptSelection['id'] = id;
			var elementId = currentPromptSelection['elementId'];
			
			mappingData[elementId]['mappings'].push(currentPromptSelection)
			/*{subjectVariable: currentPromptSelection.subjectVariable,
			property : currentPromptSelection.property,
			variable : currentPromptSelection.variable,
			selectedResourceUri = currentPromptSelection.selectedResourceUri
			value : textSelection
			});*/
			// update the display
			// for some reason, jquery can't select the element with the id created dynamically
			// so just navigating the dom from the button that was selected (which is a hack, and not a good one)
			$(currentPromptSelection.element.parentNode.parentNode)
					.append(
							"<div class=\"col-md-12 prompt-response\" id=\""+id+"\">"
									+ textSelection.string
									+ " <button class='btn btn-default' onclick=\"removePromptResponse('"
									+ id + "','"+currentPromptSelection['elementId']+"');\"'>Remove</button></div>");

			var property = currentPromptSelection['property'], variable = currentPromptSelection['variable'];
			// reset the current selection and relevant display components
			resetCurrentPromptSelection();
			console.log(mappingData)
			
			// check for any questions relating to the response, and add them if necessary
			addSubtemplate("#prompt-"+elementId +"-"+property,
				elementId,
				id,
				selectedValue,
				variable);
		}

		function addSubtemplate(elementIdToAddAfter,parentTemplateId,parentMappingId, selectedValue, variable ){
			// should really do a better way to check if its a URL
			if ((typeof selectedValue === 'string' || selectedValue instanceof String) && selectedValue.startsWith("http")){
				var jqxhr = $.ajax({
					url : getTemplateForGuiURL,
					data : {
						'type' : selectedValue
					},
					type : 'GET',
					//dataType:'jsonp',
					success : function(data) {
						if (data!="{}"){
							console.log(data);
							var template = JSON.parse(data);
							var id = guid();
							var r = window.Library.buildGui(template, variable, id, false);
							// add the subtemplate link to the parent mapping
							for (var i =0; i< mappingData[parentTemplateId]['mappings'].length;i++){
								if (mappingData[parentTemplateId]['mappings'][i]['id'] == parentMappingId){
									mappingData[parentTemplateId]['mappings'][i]['subTemplate'] = id;
									break;
								}
							}
							
							// now display the subtemplate
							templates[id] = template;
							var div = "<div class='' id='"+id+"'>" + r + "</div>";
							$(div).insertAfter(elementIdToAddAfter);
							mappingData[id] = {'ui-id':id, 'templateType':selectedValue,'variable':variable,'mappings':[]};
							console.log(mappingData);
							return r;
						}
						return "";
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert("failed");
						console.log(textStatus);
						console.log(errorThrown)
					}
				});
			}
			
//			$("<div class=\"row\"><p>hello</p></div>").insertAfter($(elementIdToAddAfter);

		}
		
		function removePromptResponse(mappingId, elementId) {
			
			// remove any submappings
			var mappingToBeRemoved = $.grep(mappingData[elementId]['mappings'], function(i) {
				return i['id'] == mappingId;
			});
			
			if (mappingToBeRemoved.length > 0 && mappingToBeRemoved[0]['subTemplate'] != null){
				// delete the UI components
				$("#" + mappingToBeRemoved[0]['subTemplate']).remove();
				// remove the highlighted text for all the mappings
				for (var i =0;i<mappingData[mappingToBeRemoved[0]['subTemplate']]['mappings'].length;i++){
					var textHighlight = mappingData[mappingToBeRemoved[0]['subTemplate']]['mappings'][i]['id'];
					var text = $("#"+textHighlight+"-text-highlight").html();
					$("#"+textHighlight+"-text-highlight").replaceWith(text);
				}
				
				// delete the mapping data and template
				delete mappingData[mappingToBeRemoved[0]['subTemplate']]
				delete templates[mappingToBeRemoved[0]['subTemplate']]
			}
			
			// adapted from http://stackoverflow.com/questions/3596089/how-to-remove-specifc-value-from-array-using-jquery
			var m = $.grep(mappingData[elementId]['mappings'], function(i) {
				return i['id'] != mappingId;
			});
			$("#" + mappingId).remove();
			mappingData[elementId]['mappings'] = m;
			
			// remove the text highlight
			console.log("getting id #"+mappingId+"-text-highlight");
			var text = $("#"+mappingId+"-text-highlight").html();
			$("#"+mappingId+"-text-highlight").replaceWith(text);
			
			// just in case the user was responding to a question
			resetCurentPromptSelection();
		}

		// taken from http://stackoverflow.com/questions/105034/create-guid-uuid-in-javascript
		function guid() {
			function s4() {
				return Math.floor((1 + Math.random()) * 0x10000).toString(16)
						.substring(1);
			}
			return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-'
					+ s4() + s4() + s4();
		}

		function getTextAreaSelection() {
			var t = {};
			if (window.getSelection()) {
				var s = window.getSelection();
				t.start = s.anchorOffset;
				t.end = s.focusOffset;
				t.anchorNode = s.anchorNode;
				t.string = String(s);
			} else if (document.getSelection()) {
				t.string = String(document.getSelection());
				console.log("2");
			} else if (document.selection()) {
				t.string = String(document.selection.createRange().text);
				console.log("3");
			}
			console.log(t);
			return t;
		}
		
		function highlightText(textSelection, highlightId){
            
            var text = textSelection['anchorNode']['data'];
    		var t = text.substring(0,textSelection['start']) + "<span class=\"text-highlight\" id='"+highlightId+"'>" + text.substring(textSelection['start'], textSelection['end']) + "</span>"+text.substring(textSelection['end']);
    		$(textSelection['anchorNode']).replaceWith(t);
    		
            
			/*var innerHtml = textSelection.anchorNode.parentNode.innerHTML;
			var newHtml = innerHtml.substring(0,textSelection.start);
			newHtml += "<span class=\"text-highlight\" id='"+highlightId+"'>";
			newHtml += innerHtml.substring(textSelection.start, textSelection.end)
			newHtml += "</span>";
			newHtml += innerHtml.substring(textSelection.end);
			textSelection.anchorNode.parentNode.innerHTML = newHtml;*/
		}

		// Update the resource selection display (formally the control panel) to display suitable options
		// for the type(s) specified in the types variable array
		function displayResourceSelection(types) {
			console.log("display options for " + appropriateTypes)
		}

		function displayGraph() {
			console.log("setting up graph");
			// $.ajax({
			//   url: "link.php?"
			// }).done(function(data) { // data what is sent back by the php page
			//  $('#canvas').html(data); // display data

			$('#graph').html("");

			setUP();

			// app starts here
			svg.on('mousedown', mousedown).on('mousemove', mousemove).on(
					'mouseup', mouseup);
			d3.select(window).on('keydown', keydown).on('keyup', keyup);
			restart();

			console.log("hello");
			// });
		}

        // returns an object with fields: numberQuestions: X, numberResponses: Y
        // where x is the number of questions displayed, and y the number that have a response
        // for templates associated with variable
        function generateHeaderSummary(variable){
            // get the ids of mappings/templates associated with the variable
            var ids = getTemplateIds('variable',variable);
            var result = {"numberQuestions":0, "numberResponses":0};
            $.each(ids, function(i){
                generateTemplateSummary(ids[i], result, variable)
            });
            return result;
        }

        // mappingId - same as templateId
        function generateTemplateSummary(mappingId, result, variable){
            var properties = window.Library.getProperties(templates[mappingId], variable);
            $.each(properties, function(index){
                result['numberQuestions'] += 1;
                var found =  getElementFromArray(mappingData[mappingId]['mappings'], "property", properties[index]['property']);
                if (found != null) {
                    result['numberResponses'] += 1;
                    if (found['subTemplate']){
                        generateTemplateSummary(found['subTemplate'],result,mappingData[found['subTemplate']]['variable'] );
                    }
                }
            });
        }

        // generates the HTML for the dialog box that provides a summary of the questions responded to / left
		function generateReportCard(){
            var msgs = [];
            
            msgs.push($("#textArea").clone());
            
            // first the datasets, then the data prep activities, then the data analysis
            var toCheck = ['vargen:rawDatasetId', 'vargen:dataprepActivityId','vargen:dataAnalysisActivityId']
            var displayNames = ["Datasets", "Data Preparation", "Data Analysis"];
            var overallScore = {'numberResponses':0, 'numberQuestions':0}
            $.each(toCheck, function(i){
                var ids = getTemplateIds('variable',toCheck[i]);
                msgs.push("<h3>"+displayNames[i]+"</h3>")
                $.each(ids, function(id){
                    msgs.push("<strong>Alerts</strong>")
                    var labelMapping = getElementFromArray(mappingData[ids[id]]['mappings'], 'property','label');
                    if (labelMapping!=null){
                        msgs.push(" - " + labelMapping['valueLabel']['string']);
                    }
                    var score = {"numberQuestions":0, "numberResponses":0};
                    generateTemplateSummary(ids[id], score, mappingData[ids[id]]['variable']);
                    msgs.push(" - " + score['numberResponses'] +" / " + score['numberQuestions'])
                    overallScore['numberResponses'] += score['numberResponses'];
                    overallScore['numberQuestions'] += score['numberQuestions'];
                    msgs.push("<ul>")
                    msgs = msgs.concat(generateMappingReportCard(templates[ids[id]], mappingData[ids[id]], mappingData[ids[id]]['variable']));
                    msgs.push("</ul>")
                }); 
            });
            msgs.push("<p>Overall score: " + overallScore['numberResponses'] + "/" + overallScore['numberQuestions'] + "</p>");
              
//            $.each(templates, function(key, value){
//				var template = templates[key];
//				var mappings = mappingData[key];
//				msgs = msgs.concat(generateMappingReportCard(template, mappings, mappings['variable']))
//			});
            var db = new PouchDB('socialprov');
            var content = "";
            var reportId = guid();
            $.each(msgs, function(index, item){content+=item;});
            var page = {'_id':reportId, 'content':content};
            db.put(page);
            window.open("report.html?id="+reportId);
            
            
            $("#dialog-summary-content").html(msgs);
            $("#dialog-summary").modal('show');
            console.log(msgs);
		}
            
        // gets the IDs of templates from the templates variable, where each returned template has a property property
            // which has a value value
        function getTemplateIds(property, value){
            var ids = [];
            $.each(mappingData, function(key, v){
               if (v[property] == value){
                   ids.push(key);
               } 
            });
            return ids;
        }
            
        function generateMappingReportCard(template, mappings, variable){
            var msgs = [];
            var properties = window.Library.getProperties(template, variable);
            $.each(properties, function(index){
                var found =  getElementFromArray(mappings['mappings'], "property", properties[index]['property']);
                if (found == null){
                    msgs.push("<li>No information provided in response to: " + properties[index]['label']+"</li>")
                } else {
                    //msgs.push("<li>For &quot;" + properties[index]['label'] + "&quot; the provided response is: &quot;" + found['valueLabel']['string'] +"&quot;</li>");
                    if (found['subTemplate']){
                        msgs = msgs.concat(generateMappingReportCard(templates[found['subTemplate']], mappingData[found['subTemplate']],mappingData[found['subTemplate']]['variable'] ));
                    }
                }
            });
            return msgs;
        }
            
		// array is an array of JSON objects
		// property is a property to check each object for
		// if the value of property for an object in array is value, then that object is returned
		function getElementFromArray(array, property, value){
			var item = null;
			$.each(array, function(index){
				if (array[index][property] == value){
					item = array[index];
					return;
				}
			})
			return item;
		}


		