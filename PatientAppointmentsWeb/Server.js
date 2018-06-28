var express         =         require("express");
var app             =         express();

var sql = require('mssql');

var config = {
    user: 'sa',
    password: 'sqladmin',
    server: 'VICTORS-LEGACY\\SQLEXPRESS',
    database: 'PatientAppointments'
};

var bodyParser = require('body-parser');
app.use(bodyParser()); 

/*
  * Configure Express Server.
*/
app.use('/scripts', express.static(__dirname + '/app/scripts'));
app.use('/bower_components',  express.static(__dirname + '/bower_components'));
app.use('/css', express.static(__dirname + '/app/css'));
app.use('/images', express.static(__dirname + '/app/images'));
app.use('/views', express.static(__dirname + '/app/views'));
app.use('/templates', express.static(__dirname + '/app/templates'));

/*
  * Define routing of the application.
*/
app.get('/',function(req,res){
    res.sendfile('app/index.html');
});

app.get('/appointments/:mrn',function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 

        var request = new sql.Request();
        request.stream = true; // You can set streaming differently for each request 
        request.query( 'SELECT pai.appointmentId as appointmentId, pai.patientMedicalRecordNumber as patientMedicalRecordNumber, di.doctorFirstName + \' \' + di.doctorLastName + \' ( \' + di.doctorSpecialization + \', \' + di.doctorDescription + \' )\' AS doctorid, CONVERT(VARCHAR(24),pai.dayRequiredForAppointment,101) as dayRequiredForAppointment, pai.appointmentTimeHours as appointmentTimeHours, pai.appointmentTimeMinutes as appointmentTimeMinutes, pai.ailmentDescription as ailmentDescription, pai.appointmentStatus AS appointmentStatus, pai.rescheduleDate AS rescheduleDate, pai.rescheduleTimeHours AS rescheduleTimeHours, pai.rescheduleTimeMinutes AS rescheduleTimeMinutes FROM DoctorInfo di, PatientAppointmentInfo pai WHERE pai.doctorid = di.doctorid AND pai.patientMedicalRecordNumber = ' + req.params.mrn + ' order by dayRequiredForAppointment desc'); // or request.execute(procedure); 

        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 

        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            res.end(JSON.stringify(data));
        });

    });

    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
    
});

app.get('/doctorSchedule/:doctorId',function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 

        var request = new sql.Request();
        request.stream = true; // You can set streaming differently for each request 
        request.query( 'select dayRequiredForAppointment as scheduleDate, count(doctorId) as appointmentCount from PatientAppointmentInfo group by dayRequiredForAppointment, doctorid having doctorid = \'' + req.params.doctorId + '\'' ); // or request.execute(procedure); 

        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 
            //console.log(columns);
        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            res.end(JSON.stringify(data));
        });

    });

    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
    
});

app.get('/bookedSlots/:doctorId/:dayRequiredForAppointment', function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 
        var request = new sql.Request();
        request.stream = true;
        request.input('doctorId', sql.VarChar(50), req.params.doctorId);
        request.input('dayRequiredForAppointment', sql.Date, req.params.dayRequiredForAppointment);
        request.query('SELECT distinct appointmentTimeHours FROM  PatientAppointmentInfo WHERE doctorId=@doctorId AND dayRequiredForAppointment=@dayRequiredForAppointment order by appointmentTimeHours');
        
        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 
           
        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row.appointmentTimeHours);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            res.end(JSON.stringify(data));
        });

    });

    //res.end(JSON.stringify('true'));
    
    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});


app.get('/checknearappointments/:medicalRecordNumber/:dayRequiredForAppointment/:appointmentTimeHours', function(req,res){
    
    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 
        var request = new sql.Request();
        request.stream = true;
        request.input('medicalRecordNumber', sql.Numeric (18, 0), req.params.medicalRecordNumber);
        request.input('dayRequiredForAppointment', sql.Date, req.params.dayRequiredForAppointment);
        request.input('appointmentTimeHoursLow',sql.Numeric (2, 0),  parseInt(req.params.appointmentTimeHours, 10) - 2);
        request.input('appointmentTimeHoursHigh', sql.Numeric (2, 0), parseInt(req.params.appointmentTimeHours, 10) + 2);
        request.query('SELECT CONVERT(VARCHAR(10),pa.dayRequiredForAppointment,101) as dayRequiredForAppointment, pa.appointmentTimeHours as appointmentTimeHours, pa.appointmentTimeMinutes as appointmentTimeMinutes, (select di.doctorFirstName + \' \' + di.doctorLastName from doctorinfo di where di.doctorid = pa.doctorid) as doctorId FROM  PatientAppointmentInfo pa WHERE pa.patientMedicalRecordNumber=@medicalRecordNumber AND pa.dayRequiredForAppointment=@dayRequiredForAppointment AND pa.appointmentTimeHours BETWEEN @appointmentTimeHoursLow AND @appointmentTimeHoursHigh order by pa.appointmentTimeHours');
        
        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 
           
        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            res.end(JSON.stringify(data));
        });

    });

    //res.end(JSON.stringify('true'));
    
    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});

app.post('/login', function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 

        var request = new sql.Request();
        request.stream = true; // You can set streaming differently for each request 
        request.query( 'OPEN SYMMETRIC KEY SymmetricKey1 DECRYPTION BY CERTIFICATE Certificate1 SELECT medicalRecordNumber, firstName, lastName, dateOfBirth, userEmailAddress,CONVERT(varchar, DecryptByKey(applicationPasswordEnc)) AS applicationPassword FROM ApplicationUserInfo WHERE userEmailAddress = \'' + req.body.email + '\' AND isAdmin = \'N\' CLOSE SYMMETRIC KEY SymmetricKey1' ); // or request.execute(procedure); 

        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 
   
        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            if(data.length === 0) {

                data.push({});
            } else if ( data[0].applicationPassword === req.body.password ) {
                
                res.end(JSON.stringify(data[0]));
            }
            res.end(JSON.stringify(''));
        });

    });
    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});

app.get('/doctors', function(req,res){
    
    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 

        var request = new sql.Request();
        request.stream = true; // You can set streaming differently for each request 
        request.query( 'SELECT * FROM DoctorInfo' ); // or request.execute(procedure); 

        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 

        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            res.end(JSON.stringify(data));
        });

    });
    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});

app.post('/newappointment', function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 
        var request = new sql.Request();
        request.stream = true;
        var appointmentId = req.body.medicalRecordNumber + '_' + (new Date()).getTime();
        request.input('appointmentId', sql.VarChar(50), appointmentId);
        request.input('medicalRecordNumber', sql.Numeric (18, 0), req.body.medicalRecordNumber);
        request.input('doctorId', sql.VarChar(50), req.body.doctorId);
        request.input('dayRequiredForAppointment', sql.Date, req.body.dayRequiredForAppointment);
        request.input('appointmentTimeHours', sql.Numeric (2, 0), req.body.appointmentTimeHours);
        request.input('appointmentTimeMinutes', sql.Numeric (2, 0), req.body.appointmentTimeMinutes);
        request.input('ailmentDescription', sql.VarChar(sql.MAX), req.body.ailmentDescription);
        request.query('INSERT INTO PatientAppointmentInfo(appointmentId,patientMedicalRecordNumber,doctorId,dayRequiredForAppointment,appointmentTimeHours,appointmentTimeMinutes,ailmentDescription,rescheduleDate,rescheduleTimeHours,rescheduleTimeMinutes,appointmentStatus)VALUES ( @appointmentId,@medicalRecordNumber,@doctorId,@dayRequiredForAppointment,@appointmentTimeHours,@appointmentTimeMinutes,@ailmentDescription, null, 0, 0, 1 )');
        
        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 

        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            var data = [affected, appointmentId];
            res.end(JSON.stringify(data));
        });

    });

    //res.end(JSON.stringify('true'));
    
    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});

app.post('/registeruser', function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 
        var request = new sql.Request();
        request.stream = true;
        request.input('firstName', sql.VarChar(50), req.body.firstName);
        request.input('lastName', sql.VarChar(50), req.body.lastName);
        request.input('dob', sql.Date, req.body.dob);
        request.input('email', sql.VarChar(50), req.body.email);
        request.input('password', sql.VarChar(50), req.body.password);
        request.query('OPEN SYMMETRIC KEY SymmetricKey1 DECRYPTION BY CERTIFICATE Certificate1 INSERT INTO ApplicationUserInfo(medicalRecordNumber,firstName,lastName,dateOfBirth,userEmailAddress,applicationPasswordEnc,isAdmin) VALUES ((select MAX(medicalRecordNumber) + 1 FROM ApplicationUserInfo),@firstName,@lastName,@dob,@email,EncryptByKey( Key_GUID(\'SymmetricKey1\'), CONVERT(varchar,@password) ),\'N\') CLOSE SYMMETRIC KEY SymmetricKey1'); 

        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 
          
        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
            res.end(JSON.stringify(affected));
        });
    });

    //res.end(JSON.stringify('true'));

    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});


app.post('/rescheduleappointment', function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 
        var request = new sql.Request();
        request.stream = true;
        request.input('inputAppointmentId', sql.VarChar(50), req.body.inputAppointmentId);
        request.input('inputAppointmentDate', sql.Date, req.body.inputAppointmentDate);
        request.input('inputAppointmentHrs', sql.Numeric (2, 0), req.body.inputAppointmentHrs);
        request.input('inputAppointmentMin', sql.Numeric (2, 0), req.body.inputAppointmentMin);
        request.query('UPDATE PatientAppointmentInfo SET appointmentStatus=15,rescheduleDate=@inputAppointmentDate, rescheduleTimeHours=@inputAppointmentHrs,rescheduleTimeMinutes=@inputAppointmentMin WHERE appointmentId=@inputAppointmentId');

        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 
          
        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
     
            res.end(JSON.stringify(affected));
        });
    });

    //res.end(JSON.stringify('true'));

    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});

app.post('/cancelappointment', function(req,res){

    var data = [];
    sql.connect(config, function(err) {
        // ... error checks 
        var request = new sql.Request();
        request.stream = true;
        request.input('appointmentId', sql.VarChar(50), req.body.appointmentId);
        request.query('UPDATE PatientAppointmentInfo SET appointmentStatus = 20 WHERE appointmentId = @appointmentId');

        request.on('recordset', function(columns) {
            // Emitted once for each recordset in a query 

        });

        request.on('row', function(row) {
            // Emitted for each row in a recordset 
            data.push(row);
        });

        request.on('error', function(err) {
            // May be emitted multiple times 
            console.log(err);
        });

        request.on('done', function(affected) {
            // Always emitted as the last one 
            sql.close();
    
            res.end(JSON.stringify(affected));
        });
    });

    //res.end(JSON.stringify('true'));

    sql.on('error', function(err) {
        // ... error handler
        console.log(err);
    });
});
/*
  * Start the Express Web Server.
*/
app.listen(3000,function(){
  console.log("Listening to PORT 3000");
});
