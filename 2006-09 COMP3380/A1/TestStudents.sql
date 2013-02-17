SELECT * FROM Students;

SELECT * FROM Courses;

SELECT * FROM Enrolled;

INSERT INTO Courses VALUES ('COMP 4060', 'Topics in CS');
SELECT * FROM Courses;

DELETE FROM Courses WHERE courseID='COMP 4060';
SELECT * FROM Courses;

Insert into Students VALUES(8000000, 'James Bond');
SELECT * FROM Students;

Update Students SET studentName='Jesse James' WHERE studentID=8000000;
SELECT * FROM Students;

Delete FROM Students WHERE studentID = 8000000;
SELECT * FROM Students;

Delete FROM Students WHERE studentID = 9000000;
SELECT * FROM Students;

SELECT Students.studentName, Courses.courseName, Enrolled.grade 
FROM Students, Courses, Enrolled
WHERE Students.studentID=Enrolled.studentID AND
      Courses.courseID=Enrolled.courseID;

