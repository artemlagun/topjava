const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

let filter = "";

function getFilter() {
    $.ajax({
        url: ctx.ajaxUrl + "filter/?" + $("#filterAdjustments").serialize(),
        type: "GET"
    }).done(function (data) {
        updateTable(data)
        filter = "filter/?" + $("#filterAdjustments").serialize();
    });
}

function updateData() {
    $.get(ctx.ajaxUrl + filter, function (data) {
        updateTable(data);
    });
}
