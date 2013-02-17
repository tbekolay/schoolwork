set classpath=.;.\hsqldb.jar;%classpath%
java org.hsqldb.util.SqlTool --rcfile sqltool.rc Students TestStudents.sql > a1q3BatchSQL.log
