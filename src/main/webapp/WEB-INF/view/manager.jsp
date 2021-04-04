<%@ page import="static pet.kozhinov.iron.utils.Constants.API_PREFIX" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Manager index page</title>
        <link href="<c:url value="/resources/css/manager.css"/>" rel="stylesheet"/>
    </head>
    <body>
        <div class="wrapper">
            <div class="feature-container">
                <h3>Make a new loan offer</h3>
                <form id="create-loan-offer" action="javascript:void(0);" onsubmit="createOffer()">
                    <label>Client: <select id="create-loan-offer__clients"></select></label>
                    <label>Loan: <select id="create-loan-offer__loans"></select></label>

                    <label>Amount:
                        <input id="create-loan-offer__amount" type="number" min="1" required placeholder="amount"/></label>
                    <label>Duration, months:
                        <input id="create-loan-offer__duration" type="number" min="3" required placeholder="duration, months"/></label>

                    <input type="submit" value="submit"/>
                    <input type="reset" value="reset"/>
                </form>
            </div>
            <hr>
            <div class="feature-container">
                <h3>Not accepted yet loan offers</h3>
                <div id="pending-cases-container" class="cases-container"></div>
            </div>
            <hr>
            <div class="feature-container">
                <h3>Accepted loan offers</h3>
                <div id="in-progress-cases-container" class="cases-container"></div>
            </div>
        </div>
    </body>
    <script src="<c:url value="/resources/js/jquery-3.6.0.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/dto.js"/>" type="text/javascript"></script>
    <script type="text/javascript">
        window.onload = function() {
            updatePending();
            updateInProgressCases();

            loadClients();
            loadLoans();
        }

        function offerToHtml(offer) {
            return "<div class='offer__info'>" +
                    "<div>" +
                        "<h4>Whom is offered</h4>" +
                        "<p>Last name: " + offer.client.lastName + "</p>" +
                        "<p>First name: " + offer.client.firstName + "</p>" +
                        (offer.client.patronymic ? "<p>Patronymic: " + offer.client.patronymic + "</p>" : "") +
                        "<p>Email: " + offer.client.email + "</p>" +
                        (offer.client.phoneNumber ? "<p>Phone number: " + offer.client.phoneNumber + "</p>" : "") +
                    "</div>" +
                    "<div>" +
                        "<h4>What is offered</h4>" +
                        "<p>Loan amount/max amount: " + offer.amount.toString() + "/" + offer.loan.maxAmount.toString() + "</p>" +
                        "<p>Interest rate: " + offer.loan.interestRate.toString() + "%</p>" +
                        "<p>Duration, months: " + offer.durationMonths.toString() + "</p>" +
                    "</div>" +
                "</div>";
        }

        function updatePending() {
            $.ajax({
                type: "GET",
                url: "<%=API_PREFIX%>/loan-cases/pending",
                success: function(answer) {
                    let notAcceptedActualOffersContainer = document.getElementById("pending-cases-container");
                    notAcceptedActualOffersContainer.innerHTML = "";
                    answer.forEach(function(offer, id) {
                        notAcceptedActualOffersContainer.innerHTML +=
                            "<div class='offer'>" +
                                offerToHtml(offer) +
                                "<button onclick=\"cancelOffer('" + offer.id.toString() + "')\">Cancel</button>" +
                            "</div>";
                    });
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function updateInProgressCases() {
            $.ajax({
                type: "GET",
                url: "<%=API_PREFIX%>/loan-cases/in-progress",
                success: function(answer) {
                    let acceptedActualOffersContainer = document.getElementById("in-progress-cases-container");
                    answer.forEach(function(offer, id) {
                        acceptedActualOffersContainer.innerHTML +=
                            "<div class='offer'>" +
                                offerToHtml(offer) +
                            "</div>";
                    });
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function cancelOffer(offerId) {
            $.ajax({
                type: "DELETE",
                url: "<%=API_PREFIX%>/loan-cases/" + offerId,
                success: function () {
                    updatePending();
                },
                error: function () {
                    alert("Error while deleting data");
                }
            });
        }

        function loadClients() {
            $.ajax({
                type: "GET",
                url: "<%=API_PREFIX%>/persons/?role=client",
                success: function(answer) {
                    let list = document.getElementById("create-loan-offer__clients");
                    list.innerHTML = "";
                    answer.forEach(function(client, id) {
                        list.innerHTML += "<option value='" + client.id + "'>" + client.firstName + " " +
                            client.lastName + (client.patronymic ? " (" + client.patronymic + ")" : "") + "</option>";
                    });
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function loadLoans() {
            $.ajax({
                type: "GET",
                url: "<%=API_PREFIX%>/loans/",
                success: function(answer) {
                    let list = document.getElementById("create-loan-offer__loans");
                    list.innerHTML = "";
                    answer.forEach(function(loan, id) {
                        list.innerHTML +=
                            "<option value='" + loan.id + "'>amount range: [" + loan.minAmount.toString() + " - " +
                            loan.maxAmount.toString() +"]; duration (months) range: [" + loan.minDurationMonths + " - " +
                            loan.maxDurationMonths + "]; interest rate: " + loan.interestRate.toString() + "%</option>";
                    });
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }

        function createOffer() {
            let offer = new LoanCaseDto(null,
                document.getElementById("create-loan-offer__clients").value,
                document.getElementById("create-loan-offer__loans").value,
                document.getElementById("create-loan-offer__amount").value,
                document.getElementById("create-loan-offer__duration").value,
                "APPROVED", "PENDING", null, null, null, false, null);

            $.ajax({
                type: "POST",
                url: "<%=API_PREFIX%>/loan-cases/",
                data: JSON.stringify(offer),
                contentType: "application/json",
                dataType: "json",
                success: function() {
                    document.getElementById("create-loan-offer__amount").value = "";
                    document.getElementById("create-loan-offer__duration").value = "";
                    updatePending();
                    alert("Success!");
                },
                error: function(xhr, status, error) {
                    alert("Error - " + xhr.status + ": " + xhr.statusText);
                }
            });
        }
    </script>
</html>
