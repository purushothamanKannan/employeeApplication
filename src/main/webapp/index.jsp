<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta charset="ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            color: #36919c;
        }

        .form-container {
            width: 50%;
            margin: 0 auto;
            padding: 30px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            margin-bottom: 20px;
            border-collapse: collapse;
        }

        td {
            padding: 12px;
            font-size: 16px;
        }

        input[type="text"], input[type="number"], input[type="date"] {
            width: 100%;
            padding: 8px;
            margin: 5px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        input[type="submit"] {
            width: 30%;
            margin-left: 40%;
            padding: 10px;
            background-color: #36919c;
            border: none;
            color: white;
            font-size: 16px;
            cursor: pointer;
            border-radius: 4px;
        }

        input[type="submit"]:hover {
            background-color: #0c484fka;
        }

        .link-container {
            text-align: center;
            margin-top: 20px;
        }

        .link-container a {
            font-size: 16px;
            text-decoration: none;
            color: #36919c;
        }

        .link-container a:hover {
            text-decoration: underline;
        }

        .loading {
            display: none;
            text-align: center;
            margin-top: 20px;
            font-size: 16px;
            color: #36919c;
        }
        .add_item_button {
            width: 100%;
            padding: 6px;
            padding-left: 8px;
            padding-right: 8px;
            background-color: #36919c;
            border: none;
            color: white;
            cursor: pointer;
            border-radius: 4px;
        }
        .remove_item_button {
            width: 100%;
            padding: 6px;
            padding-left: 8px;
            padding-right: 8px;
            background-color: #36919c;
            border: none;
            color: white;
            cursor: pointer;
            border-radius: 4px;
        }

        .p-image {
          position: absolute;
          top: 167px;
          right: 30px;
          color: #666666;
          transition: all .3s cubic-bezier(.175, .885, .32, 1.275);
        }
        .p-image:hover {
          transition: all .3s cubic-bezier(.175, .885, .32, 1.275);
        }



        .loader {
            margin-left: 45%;
            width: 48px;
            height: 48px;
            border: 5px solid #000000;
            border-bottom-color: transparent;
            border-radius: 50%;
            display: inline-block;
            box-sizing: border-box;
            animation: rotation 1s linear infinite;
            }

            @keyframes rotation {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }




    </style>
</head>
<body>
    <div class="form-container">
        <h2>Employee Information</h2>
        <form id="employeeForm">
            <table>
                <tr>
                    <td>Upload a profile pic</td>
                        <td>
                            <input type="file" id="image" name="image" accept="image/*" required />
                        </td>
                </tr>
                <tr>
                    <td>Employee Name</td>
                    <td><input type="text" name="employeeName" id="employeeName" maxlength="20" placeholder="Enter your Name" required></td>
                </tr>
                <tr>
                    <td>Employee Age</td>
                    <td><input type="number" name="employeeAge" id="employeeAge" placeholder="Enter your age" maxlength="2" required min="20" max="70"></td>
                </tr>
                <tr>
                    <td>Employee Experience</td>
                    <td><input type="number" name="employeeExperieance" id="employeeExperieance" placeholder="Enter your Experience in years" min="1" max="50" required></td>
                </tr>
                <tr>
                    <td>Employee Date of Joining</td>
                    <td><input type="date" name="employeeDateOfJoin" id="employeeDateOfJoin" min="2000-01-01" max="2025-12-31" required></td>
                </tr>
                <tr>
                    <td>Employee Salary</td>
                    <td><input type="number" name="employeeSalary" id="employeeSalary" placeholder="Enter your salary" min="1000" max="3000000" required></td>
                </tr>
                <tr>
                    <td>Employee Qualification</td>
                    <td><input type="text" name="employeeQualification" id="employeeQualification" placeholder="Enter your Qualification" maxlength="20" pattern="^(?![.])([A-Za-z\s.]*[A-Za-z\s])*$" title="Enter valid qualification" required></td>
                     <td colspan="3">
                        <input type="button" class="add_item_button" id="add_item_button" name="add_item_button" value="+">
                     </td>
                </tr>
                <tr id="qualificationList"><td></td></tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" id='submitValue' value="Add Employee">
                    </td>
                </tr>
            </table>
        </form>
        <span id="loadingMessage" class="loader"></span>
        <div id="EmployeeList" class="link-container">
                <a href="display.jsp">Employee List</a>
        </div>
    </div>

    <div id="response"></div>
    <script>
        window.onload = function() {
            const today = new Date();
            const year = today.getFullYear();
            const month = ("0" + (today.getMonth() + 1)).slice(-2);
            const day = ("0" + today.getDate()).slice(-2);
            const maxDate = `${year}-${month}-${day}`;
            document.getElementById('employeeDateOfJoin').setAttribute('max', maxDate);
            $('#loadingMessage').hide();
        };
        $(document).ready(function(){
            $('#employeeName').keyup(function(){
            });
            $('#add_item_button').click(function(e){
                e.preventDefault();
                var newRow = `<tr>
                                 <td><input type="text" name="employeeQualification" id="employeeQualification" placeholder="Enter your Qualification" maxlength="20" pattern="^(?![.])([A-Za-z\s.]*[A-Za-z\s])*$" title="Enter valid qualification" required></td>
                                 <td colspan="1">
                                    <input type="button" class="remove_item_button" value="-" onclick="removeQualification(this)">
                                 </td>
                             </tr>`;
                $('#qualificationList').append(newRow);
            });
            window.removeQualification = function(button) {
                $(button).closest('tr').remove();
            };

            $('#employeeForm').submit(function(e){
                e.preventDefault();
                $('#loadingMessage').show();
                $('#submitValue').hide();
                $('#EmployeeList').hide();
                $('#employeeForm').find('input, button').prop('disabled', true);

                var qualification = []
                qualification.push($('#employeeQualification').val())
                 $('#qualificationList input[type="text"]').each(function(){
                    qualification.push($(this).val());
                });
                var url = []
                url.push($('#image')[0].files[0])


                 var formData = new FormData();
                    formData.append('employeeName', $('#employeeName').val());
                    formData.append('employeeAge', $('#employeeAge').val());
                    formData.append('employeeExperieance', $('#employeeExperieance').val());
                    formData.append('employeeDateOfJoin', $('#employeeDateOfJoin').val());
                    formData.append('employeeSalary', $('#employeeSalary').val());
                    formData.append('employeeQualifications', qualification.join(", "));

                 var fileInput = $('#image')[0].files[0];
                 if (fileInput) {
                     formData.append('imageURL', fileInput);
                 }

                $.ajax({
                    url: 'Register',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function(response) {
                        $('#loadingMessage').show();
                        location.assign("display.jsp");
                        $('#loadingMessage').hide();
                        $('#employeeForm').find('input, button').prop('disabled', false);
                    },
                    error: function(xhr, status, error) {
                         $('#loadingMessage').hide();
                         $('#employeeForm').find('input, button').prop('disabled', false);
                         $('#submitValue').show();
                         $('#EmployeeList').show();
                        alert("Data not stored !")
                    }
                });
            });
        });
    </script>
</body>
</html>
