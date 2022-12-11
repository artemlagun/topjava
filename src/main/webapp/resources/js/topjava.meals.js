const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            let json = JSON.parse(stringData);
            $(json).each(function () {
                this.dateTime = this.dateTime.replace('T', ' ').substring(0, 16);
            });
            return json;
        }
    }
});

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
              "url": mealAjaxUrl,
              "dataSrc": ""
            },
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
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if(!data.excess) {
                    $(row).attr("data-meal-excess", false);
                } else {
                    $(row).attr("data-meal-excess", true)
                }
            }
        })
    );

    $.datetimepicker.setLocale(locale);

    let startDate = $('#startDate');
    let endDate = $('#endDate');

    startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
        onShow: function () {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    });

    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
        onShow: function () {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    });

    $('#startTime').datetimepicker({
        datepicker: false,
        format: 'H:i'
    });

    $('#endTime').datetimepicker({
        datepicker: false,
        format: 'H:i'
    });


    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
});



