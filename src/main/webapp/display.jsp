<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Search</title>
    <style>
        body {font-family: Arial, sans-serif; background-color: #f4f7f6; margin: 0; padding: 0;}
        .container {width: 80%; margin: 0 auto; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);}
        h2 {text-align: center; color: #36919c;}
        table {width: 100%; border-collapse: collapse; margin: 20px 0;}
        th, td {padding: 10px; text-align: left; border: 1px solid #ddd;}
        th {background-color: #36919c; color: white;}
        input[type='text'], input[type='submit'] {padding: 10px; font-size: 16px; width: 200px; margin-bottom: 20px; border: 1px solid #ddd; border-radius: 4px;}
        input[type='submit'] {background-color: #36919c; color: white; cursor: pointer;}
        input[type='submit']:hover {background-color: #0c484f;}
        button {padding: 10px 20px; font-size: 16px; cursor: pointer; background-color: #f03355; color: white; border: none; border-radius: 4px;}
        button:hover {background-color: #0c484f;}
        .link-container {text-align: center; margin-top: 20px;}
        .link-container a {font-size: 16px; text-decoration: none; color: #36919c;}
        .link-container a:hover {text-decoration: underline;}
        .loader {
          width: 60px;
          aspect-ratio: 1;
          border-radius: 50%;
          animation: l11 2s infinite;
          margin-left: 45%;
        }
        @keyframes l11 {
          0%   {background: conic-gradient(#f03355 0     ,#0000 0)}
          12.5%{background: conic-gradient(#f03355 45deg ,#0000 46deg)}
          25%  {background: conic-gradient(#f03355 90deg ,#0000 91deg)}
          37.5%{background: conic-gradient(#f03355 135deg,#0000 136deg)}
          50%  {background: conic-gradient(#f03355 180deg,#0000 181deg)}
          62.5%{background: conic-gradient(#f03355 225deg,#0000 226deg)}
          75%  {background: conic-gradient(#f03355 270deg,#0000 271deg)}
          87.5%{background: conic-gradient(#f03355 315deg,#0000 316deg)}
          100% {background: conic-gradient(#f03355 360deg,#0000 360deg)}
        }


        .employee-image {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 5px;
        }

    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

    <div class="container">
        <h2>Search Employee</h2>

        <form id="searchForm">
            <!--<label for="employeeName">Search Employee by Name: </label>-->
            <input type="text" id="employeeName" name="employeeName" placeholder="Search by Name">
            <input type="text" name="employeeSalary" id="employeeSalary" oninput="this.value = this.value.replace(/[^0-9]/g, '')" placeholder="Search by salary" >
        </form>

        <div class="link-container">
            <a href="index.jsp">Employee Add</a>
        </div>

        <table id="employeeTable">
            <thead>
                <tr>
                    <th>EmpId</th>
                    <th>EmpName</th>
                    <th>Salary</th>
                    <th>Experience</th>
                    <th>Date of Join</th>
                    <th>Age</th>
                    <th>Qualification</th>
                    <th>Profile</th>
                    <th>Remove</th>
                </tr>
            </thead>
            <tbody id="employeeName employeeSalary"></tbody>
        </table>
        <div id="loading" class="loader"></div>
    </div>

    <script>
        $(document).ready(function(){
            $('#clearSearch').click(function(f){
                document.getElementsByName('employeeName')[0].value = '';
                $('#searchForm').submit();
            });

            $('#employeeName').keyup(function(keyUp){
                $('#searchForm').submit();
            });
            $('#employeeSalary').keyup(function(keyUp){
                $('#searchForm').submit();
            });

            $('#searchForm').submit(function(e){
                e.preventDefault();
                var formData = {employeeName: $('#employeeName').val(), employeeSalary: $('#employeeSalary').val()};
                $('#loading').show();
                $('#employeeTable tbody').hide();

                $.ajax({
                    url: "Register",
                    type: "GET",
                    data: formData,
                    success: function(data) {
                    $('#employeeTable tbody').empty();
                    $('#loading').hide();
                    $('#employeeTable tbody').show();

                        data.forEach(function(item) {
                            var row = '<tr>' +
                                          '<td>' + item.empId + '</td>' +
                                          '<td>' + item.employeeName + '</td>' +
                                          '<td>' + item.employeeSalary + '</td>' +
                                          '<td>' + item.employeeExperieance + '</td>' +
                                          '<td>' + item.employeeDateOfJoin + '</td>' +
                                          '<td>' + item.employeeAge + '</td>' +
                                          '<td>' + item.employeeQualifications + '</td>' +
                                          '<td><img src="' + item.imageURL + '" alt="" class="employee-image"></td>' +
                                          '<td><button type="button" class="removeEntryBtn" onclick="removeRow(' + item.empId + ')">Remove</button></td>' +
                                      '</tr>';
                            $('#employeeTable tbody').append(row);
                        });
                    }
                });
            });
        });
        function removeRow(buttonValue){
        buttonValue.preventDefault;
        alert(buttonValue + " has been removed")
        $('#loading').show();
        $('#employeeTable tbody').hide();
            var empId = {empId: buttonValue};
                $.ajax({
                    url: "Operation",
                    type: "POST",
                    data: empId,
                    success: function(data){
                        $(document).ready(function(){
                            $('#searchForm').submit();
                            $('#employeeTable tbody').empty();
                            $('#loading').hide();
                            $('#employeeTable tbody').show();
                        })
                    }
                });
        }
        $(document).ready(function(){
            $('#searchForm').submit();
        });

        </script>
</body>
</html>
