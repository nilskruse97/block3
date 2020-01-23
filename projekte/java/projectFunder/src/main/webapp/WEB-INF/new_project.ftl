<html>
<head><title>Hello World</title>
 
<body>
  <form name="user" action="hello" method="post">
    Titel: <input type="text" name="titel" /> <br/>
    Finanzierungslimit: <input type="text" name="finanzierungslimit" /> <br/>
    
	
	Kategorie: <br/>
	<#list kategorien as k>
		<input type="radio" id="${k.id}" name="${k.name}" value="${k.name}"
	         checked>
	  	<label for="${k.name}">${k.name}</label><br/>
	</#list>
	
	Vorgaenger:
    <select>
    	<#list vorgaenger as v>
        	<option value="${v.kennung}">${v.titel}</option>
    	</#list>
	</select> <br/>
	<input type="submit" value="Erstellen" />
  </form>
 
</body>
</html>