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
        <#if projekt??>
        <form name="projekt" action="edit_project?kennung=${projekt.kennung}" method="post">
            Titel: <input type="text" name="titel" value=${projekt.titel} /> <br/>
            Finanzierungslimit: <input type="text" name="finanzierungslimit" value=${projekt.finanzierungslimit} /> <br/>


            Kategorie: <br/>
	    <#list kategorien as k>
		<input type="radio" id="${k.id}" name="kategorie" value="${k.id}">
	  	<label for="kategorie">${k.name}</label><br/>
        </#list>

            Vorgaenger:<br/>
            <input type="radio" id="-1" name="vorgaenger" value="-1">
            <label for="vorgaenger">Keiner</label><br/>
	    <#list vorgaenger as v>
		<input type="radio" id="${v.kennung}" name="vorgaenger" value="${v.kennung}">
	  	<label for="vorgaenger">${v.titel}</label><br/>
        </#list><br/>

            Beschreibung: <br/>
            <textarea name="beschreibung" cols="50" rows="10">${projekt.beschreibung}</textarea>
            <br/>
            <input type="submit" value="Erstellen" />
        </form>
        </#if>
  	<#list report! as r>
        ${r}<br/>
    </#list>
    </div>
</div>
</body>
</html>