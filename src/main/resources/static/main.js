$(function(){
    $.ajax({
        type : "post",
        url : "/xingoo/classes",
        data : {pagkage:$('#packageName').val()},
        async : false,
        success : function(result){
            var data = result.data;
            var html = "";

            for (var key in data){
                html += "<option>"+data[key]+"</option>";
            }

            $('#dubboInterface').empty();
            $('#dubboInterface').html(html);

            $.ajax({
                type : "post",
                url : "/xingoo/methods",
                data : {clazz:$('#dubboInterface').val()},
                async : false,
                success : function(result){
                    var data = result.data;
                    var html = "";

                    for (var key in data){
                        html += "<option param='"+data[key]+"' >"+key+"</option>";
                    }

                    $('#dubboMethod').empty();
                    $('#dubboMethod').html(html);
                }
            });
        }
    });

    $("#dubboInterface").change(function(){
        $.post("/xingoo/methods",
            {clazz:$('#dubboInterface').val()},
            function(result){

                var data = result.data;
                var html = "";

                for (var key in data){
                    html += "<option param='"+data[key]+"' >"+key+"</option>";
                }

                $('#dubboMethod').empty();
                $('#dubboMethod').html(html);
            }
        );
    });

    $("#queryBtn").click(function(){
        $('#dubbo_table').empty();
        $.post("/xingoo/invoke",
            {
                clazz:$('#dubboInterface').val(),
                method:$('#dubboMethod').val(),
                params:$('#dubboParams').val(),
                types:$("#dubboMethod option:selected").attr("param")
            },
            function(result){
                var data = result.data;
                $('#dubbo_table').html(data);
            }
        );
    });
})