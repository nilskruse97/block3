<html>
<head><title>ProjectFunder</title>
<style type="text/css">
* {
   margin:0;
   padding:0;
}

body{
   text-align:center;
   background: #efe4bf none repeat scroll 0 0;
}

#wrapper{
   width:960px;
   margin:0 auto;
   text-align:left;
   background-color: #fff;
   border-radius: 0 0 10px 10px;
   padding: 20px;
   box-shadow: 1px -2px 14px rgba(0, 0, 0, 0.4);
}

#header{
 color: #fff;
 background-color: #2c5b9c;
 height: 3.5em;
 padding: 1em 0em 1em 1em;
 
}

#site{
    background-color: #fff;
    padding: 20px 0px 0px 0px;
}
.centerBlock{
	margin:0 auto;
}
</style>

<body>
	<div id="wrapper">
		<div id="header">
		<h1> ProjectFunder Website </h1>
		</div>
	   
		<div id="site">
		<#list report! as r>
			${r}<br/>
   		</#list>
   		<#if projekt??>
		Icon: ${projekt.kategorie.icon}<br/>
		Titel: ${projekt.titel}<br/>
		Ersteller: ${projekt.ersteller}<br/>
		Finanzierungslimit: ${projekt.finanzierungslimit}<br/>
		Spendensumme: ${projekt.spendenmenge}<br/>
		Status: ${projekt.status}<br/>
		<#if projekt.fkVorgaenger gt 0 >
			<a href="/view_project?kennung=${projekt.fkVorgaenger}">Vorgänger<a/><br/>
		</#if>
		Beschreibung: ${projekt.beschreibung}<br/>
		<a href="/new_project_fund?kennung=${projekt.kennung}">Spenden<a/><br/>
		<a href="/edit_project?kennung=${projekt.kennung}">Projekt editieren<a/><br/>
		<a href="/view_project?kennung=${projekt.kennung}&delete=1">Projekt löschen<a/><br/>
		<a href="/new_comment?kennung=${projekt.kennung}">Kommentieren<a/><br/>
		Kommentare:<br/>
		<#list projekt.kommentare as k>
	    ${k.benutzer}: ${k.kommentar}<br/>
	    </#list>
	    
	    Spenden:<br/>
		<#list projekt.spenden as s>
	    ${s.spender}: ${s.spendenbetrag}<br/>
	    </#list>
		</#if>
		</div>
		
		
	</div>
</body>
</html>
