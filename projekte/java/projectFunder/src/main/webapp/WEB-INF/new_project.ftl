<html>
<head><title>Hello World</title>
 
<body>
  <form name="projekt" action="new_project" method="post">
    Titel: <input type="text" name="titel" /> <br/>
    Finanzierungslimit: <input type="text" name="finanzierungslimit" /> <br/>
    
	
	Kategorie: <br/>
	<#list kategorien as k>
		<input type="radio" id="${k.id}" name="kategorie" value="${k.name}">
	  	<label for="kategorie">${k.name}</label><br/>
	</#list>
	
	Vorgaenger:<br/>
	<#list vorgaenger as v>
		<input type="radio" id="${v.kennung}" name="vorgaenger" value="${v.titel}">
	  	<label for="vorgaenger">${v.titel}</label><br/>
	</#list><br/>
	
	Beschreibung: <br/>
	<textarea name="beschreibung" cols="50" rows="10"></textarea>
	<br/>
	<input type="submit" value="Erstellen" />
  	</form>
  
  	<#list fehler! as f>
		${f}<br/>
    </#list>
 
</body>
</html>