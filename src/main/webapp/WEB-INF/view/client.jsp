<%@ page import="static pet.kozhinov.iron.utils.Constants.API_PREFIX" %>
<%@ page import="static pet.kozhinov.iron.utils.Constants.CURRENT_PERSON_KEYWORD" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>The Iron bank</title>
        <link href="<c:url value="/resources/css/client.css"/>" rel="stylesheet"/>
    </head>
    <body>
        <div class="wrapper">
            <h2>Greetings, ${username}</h2>
            <div class="feature-container"><div id="client-offers-container" class="cases-container"></div></div>
            <div class="feature-container"><div id="client-loans-container" class="cases-container"></div></div>
        </div>
    </body>
    <script src="<c:url value="/resources/js/jquery-3.6.0.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/dto.js"/>" type="text/javascript"></script>
    <script>
        window.onload = function() {
            updateLoans();
            updateOffers();
        }

        function offerToHtml(offer) {
            let paymentsHtml = [];
            paymentsHtml.push("<table>" +
                "<tr>" +
                    "<th>Date</th>" +
                    "<th>Amount of payment</th>" +
                    "<th>Body part of payment</th>" +
                    "<th>Percents part of payment</th>" +
                    "<th>Paid?</th>" +
                "</tr>");
            offer.payments.forEach(function (payment, id) {
                paymentsHtml.push("<tr>" +
                    "<td>" + (payment.date != null ? payment.date : "not confirmed yet") + "</td>" +
                    "<td>" + payment.amount + "</td>" +
                    "<td>" + payment.loanRepaymentAmount + "</td>" +
                    "<td>" + payment.interestRepaymentAmount + "</td>" +
                    "<td>" + (payment.paidOut === true ? "Yes" : "No") + "</td>" +
                "</tr>");
            });
            paymentsHtml.push("</table>");

            return "<div class='offer'>" +
                "<div class='offer__info'>" +
                    "<h3>Short info</h3>" +
                    "<div>" +
                        "<p>Borrow: " + offer.amount.toString() + "</p>" +
                        "<p>Interest rate: " + offer.loan.interestRate.toString() + "%</p>" +
                        "<p>Duration, months: " + offer.durationMonths.toString() + "</p>" +
                        "<p>Have to give back (±1): " + getPayBackAmount(offer) + "</p>" +
                    "</div>" +
                "</div>" +
                "<div class='offer__payments-schedule'>" +
                    "<h3>Payments schedule</h3>" +
                    paymentsHtml.join("") +
                "</div>" +
            "</div>";
        }

        function updateOffers() {
            $.ajax({
                type: "GET",
                url: "<%=API_PREFIX%>/persons/<%= CURRENT_PERSON_KEYWORD %>/offers",
                success: function(answer) {
                    let offersContainer = document.getElementById("client-offers-container");
                    offersContainer.innerHTML = "";
                    if (answer.length > 0) {
                        offersContainer.innerHTML += "<h3>Loan offers for you</h3>";
                    }
                    answer.forEach(function(offer, id) {
                        offersContainer.innerHTML +=
                            offerToHtml(offer) +
                            "<button onclick=\"acceptOffer('" + offer.id.toString() + "')\">Accept this offer</button>" +
                            "<button onclick=\"rejectOffer('" + offer.id.toString() + "')\">Reject this offer</button>";
                    });
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function updateLoans() {
            $.ajax({
                type: "GET",
                url: "<%=API_PREFIX%>/persons/<%=CURRENT_PERSON_KEYWORD%>/loans",
                success: function(answer) {
                    let loansContainer = document.getElementById("client-loans-container");
                    loansContainer.innerHTML = "";
                    if (answer.length > 0) {
                        loansContainer.innerHTML += "<h3>Your loans</h3>";
                    }
                    answer.forEach(function(offer, id) {
                        loansContainer.innerHTML +=
                            offerToHtml(offer);
                    });
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function acceptOffer(caseId) {
            $.ajax({
                type: "PATCH",
                url: "<%=API_PREFIX%>/persons/<%=CURRENT_PERSON_KEYWORD%>/offers/" + caseId.toString() +
                    "?newStatus=APPROVED",
                success: function() {
                    updateOffers();
                    updateLoans();
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function rejectOffer(offerId) {
            $.ajax({
                type: "PATCH",
                url: "<%=API_PREFIX%>/persons/<%=CURRENT_PERSON_KEYWORD%>/offers/" + offerId.toString() +
                    "/?newStatus=REJECTED",
                success: function() {
                    updateOffers();
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function getPayBackAmount(offer) {
            let sum = 0;
            offer.payments.forEach(function (payment, id) {
                sum += Number.parseFloat(payment.amount);
            });
            return sum.toFixed(3);
        }
    </script>
</html>
