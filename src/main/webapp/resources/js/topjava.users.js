const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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

function enable(id, checkbox) {
    let enabled = checkbox.is(":checked");
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + id,
        data: "enabled=" + enabled
    }).done(function () {
        checkbox.closest("tr").attr("data-userEnabled", enabled);
        successNoty(enabled ? "enabled": "disabled");
    }).fail(function () {
        $(checkbox).prop("checked", !enabled)
    });
}

function updateData() {
    $.get(ctx.ajaxUrl, function (data) {
        updateTable(data);
    });
}