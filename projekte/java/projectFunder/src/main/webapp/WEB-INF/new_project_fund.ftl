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
		<form name="projekt" action="/new_project_fund?kennung=${projekt.kennung}" method="post">
    ${projekt.titel}<br/>
    Finanzierungslimit: ${projekt.finanzierungslimit} <br/>
    Spendenbetrag: <input type="text" name="spendenbetrag" /> <br/>
    <input type="checkbox" name="sichtbarkeit" value="private"> Anonym?<br>

	<input type="submit" value="Spenden" />
  	</form>
  
  	<#list report! as r>
		${r}<br/>
    </#list>
		</div>
	</div>
</body>
</html>