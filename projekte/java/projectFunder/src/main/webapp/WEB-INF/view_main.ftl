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
	   	<a href="/new_project">Projekt erstellen<a/><br/>
	   	<a href="/view_profile">Mein Profil<a/><br/>
		<div id="site">
		Offene Projekte
		<table class="datatable">
		    <tr>
		        <th>Kategorie</th>  <th>Titel</th> <th>Ersteller</th> <th>Spendensumme</th>
		    </tr>
		    <#list projekte as projekt>
		    <tr>
		        <td><img src="${projekt.kategorie.icon}"></td> <td><a href="/view_project?kennung=${projekt.kennung}">${projekt.titel}<a/></td> <td><a href="/view_profile?nutzer=${projekt.ersteller}">${projekt.ersteller}<a/></td> <td>${projekt.spendenmenge}</td>
		    </tr>
		    </#list>
	  	</table>
	  	Geschlossene Projekte
	  	<table class="datatable">
		    <tr>
		        <th>Kategorie</th>  <th>Titel</th> <th>Ersteller</th> <th>Spendensumme</th>
		    </tr>
		    <#list projekte2 as projekt>
		    <tr>
		        <td><img src="${projekt.kategorie.icon}"></td> <td><a href="/view_project?kennung=${projekt.kennung}">${projekt.titel}<a/></td> <td><a href="/view_profile?nutzer=${projekt.ersteller}">${projekt.ersteller}<a/></td> <td>${projekt.spendenmenge}</td>
		    </tr>
		    </#list>
	  	</table>
		<tr>
		</div>
		<#list report! as r>
			${r}<br/>
   		</#list>
	</div>
</body>
</html>
