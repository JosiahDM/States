<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>States</title>
<link rel="stylesheet" href="layout.css" >
</head>
<body>
    <div class="container">
        <div id="resultsPadding"></div>
            <div id="results">
                <form action="index.html" method="post">
                    <input class="button" id="appHomeButton" type="submit" name="navTop" value="App Home" >
                </form>
                <form action="/" method="post">
                    <input class="button" id="siteHomeButton" type="submit" name="navTop" value="Site Home">
                </form>

            	<c:choose>
            		<c:when test="${! empty state}">
                        <table >
                            <tr>
                                <td>
                                    <img height=100 width="150" src="img/${state.name}.png"></img>
                                </td>
                                <td>
                                    <h2>${state.name} - ${state.abbreviation}</h2>
                                    <p>Capital: ${state.capital}</p>
                                    <p>Population: ${state.population}</p>
                                </td>
                            </tr>
                        </table>

            			<div id="gmapBorder">
                            <iframe id="gmap" width="400" height="300" frameborder="0" style="border:0"
    				                    src="https://www.google.com/maps/embed/v1/search?q=${state.latitude},${state.longitude}&key=AIzaSyD8fOjgY8WBYipnkElHMaxElGTj75eqkTc" >
        			        </iframe>
                        </div>

                        <table id="navTable">
                            <tr>
                                <td>
                                    <form action="GetStateData.do" method="POST">
                                        <input class="button" type="submit" name="previous" value="Previous">
                                    </form>

                                </td>
                                <td>
                                    <form action="GetStateData.do" method="POST">
                                        <input class="button" type="submit" name="next" value="Next">
                                    </form>
                                </td>
                            </tr>
                        </table>

            		</c:when>
            		<c:otherwise>
    			        <p>No state found</p>
            		</c:otherwise>
            	</c:choose>
            </div>
        </div>
    </div>

</body>
</html>
