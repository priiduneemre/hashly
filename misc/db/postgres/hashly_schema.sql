
/*Project:          hashly*/
/*File description: DDL & DML statements for constructing the application's initial database structure (optimized for PostgreSQL 9.2.3).*/
/*Author:           Priidu Neemre (priidu@neemre.com)*/
/*Last modified:    2014-10-07 16:57:56*/


/*1. DDL - Root-level database objects*/
/*1.1 Creation statements*/
/*(For Windows environments)*/
CREATE DATABASE HASHLY
WITH
OWNER PRIIDU_DBA
TEMPLATE TEMPLATE0
ENCODING 'UTF8'
LC_COLLATE 'English_United states.1252'
LC_CTYPE 'English_United states.1252'
CONNECTION LIMIT 100;

/*(For Linux environments)*/
CREATE DATABASE HASHLY
WITH
OWNER PRIIDU_DBA
TEMPLATE TEMPLATE0
ENCODING 'UTF8'
LC_COLLATE 'en_US.UTF-8'
LC_CTYPE 'en_US.UTF-8'
CONNECTION LIMIT 100;

/*1.2 Removal statements*/
DROP DATABASE IF EXISTS HASHLY;


/*2. DDL - Tables*/
/*2.1 Creation statements*/
CREATE TABLE ALGORITHM (
    ALGORITHM_ID            SERIAL,
    NAME                    VARCHAR(10)     NOT NULL,
    DESIGNER_NAME           VARCHAR(255)    NOT NULL,
    DIGEST_LENGTH_BITS      INTEGER         NOT NULL,
    DESCRIPTION             TEXT            NOT NULL,
    
    CONSTRAINT PK_ALGORITHM PRIMARY KEY (ALGORITHM_ID),
    CONSTRAINT AK_ALGORITHM_NAME UNIQUE (NAME),
    
    CONSTRAINT CHK_ALGORITHM_DIGEST_LENGTH_BITS_IN_RANGE CHECK (DIGEST_LENGTH_BITS BETWEEN 1 AND 4096)
);

CREATE TABLE GUEST (
    GUEST_ID                SERIAL,
    IP_ADDRESS              VARCHAR(255)    NOT NULL,
    VISIT_COUNT             INTEGER         NOT NULL    DEFAULT 1,
    
    CONSTRAINT PK_GUEST PRIMARY KEY (GUEST_ID),
    CONSTRAINT AK_GUEST_IP_ADDRESS UNIQUE (IP_ADDRESS),
    
    CONSTRAINT CHK_GUEST_VISIT_COUNT_IN_RANGE CHECK (VISIT_COUNT > 0)
);

CREATE TABLE FILE_TYPE (
    FILE_TYPE_ID            SERIAL,
    EXTENSION               VARCHAR(15)     NOT NULL,
    LABEL                   VARCHAR(255)    NOT NULL,
    
    CONSTRAINT PK_FILE_TYPE PRIMARY KEY (FILE_TYPE_ID),
    CONSTRAINT AK_FILE_TYPE_EXTENSION UNIQUE (EXTENSION),
    
    CONSTRAINT CHK_FILE_TYPE_EXTENSION_VALID CHECK (EXTENSION ~* '^(\.).+$')    
);

CREATE TABLE SOURCE_TEXT (
    SOURCE_TEXT_ID          SERIAL,
    CONTENTS                TEXT            NOT NULL,
    
    CONSTRAINT PK_SORUCE_TEXT PRIMARY KEY (SOURCE_TEXT_ID)
);

CREATE TABLE SOURCE_FILE (
    SOURCE_FILE_ID          SERIAL,
    FILE_TYPE_ID            INTEGER         NOT NULL,
    FILENAME                VARCHAR(400)    NOT NULL,
    SIZE_BYTES              BIGINT          NOT NULL    DEFAULT 0,
    
    CONSTRAINT PK_SOURCE_FILE PRIMARY KEY (SOURCE_FILE_ID),
    CONSTRAINT FK_SOURCE_FILE_FILE_TYPE_ID FOREIGN KEY (FILE_TYPE_ID) REFERENCES FILE_TYPE (FILE_TYPE_ID) ON UPDATE CASCADE,
    
    CONSTRAINT CHK_SOURCE_FILE_FILENAME_NOT_EMPTY CHECK (FILENAME <> ''),
    CONSTRAINT CHK_SOURCE_FILE_SIZE_BYTES_IN_RANGE CHECK (SIZE_BYTES >= 0)
);

CREATE TABLE RESULT_BUNDLE (
    RESULT_BUNDLE_ID        SERIAL,
    GUEST_ID                INTEGER         NOT NULL,
    PERMACODE               VARCHAR(32)     NOT NULL,
    VIEW_COUNT              INTEGER         NOT NULL    DEFAULT 0,
    
    CONSTRAINT PK_RESULT_BUNDLE PRIMARY KEY (RESULT_BUNDLE_ID),
    CONSTRAINT AK_RESULT_BUNDLE_PERMACODE UNIQUE (PERMACODE),
    CONSTRAINT FK_RESULT_BUNDLE_GUEST_ID FOREIGN KEY (GUEST_ID) REFERENCES GUEST (GUEST_ID),
    
    CONSTRAINT CHK_RESULT_BUNDLE_PERMACODE_VALID CHECK (PERMACODE ~* '^([0-9]|[a-z])*$'),
    CONSTRAINT CHK_RESULT_BUNDLE_VIEW_COUNT_IN_RANGE CHECK (VIEW_COUNT >= 0)
);

CREATE TABLE DIGEST (
    DIGEST_ID               SERIAL,
    ALGORITHM_ID            INTEGER         NOT NULL,
    RESULT_BUNDLE_ID        INTEGER         NOT NULL,
    HEX_VALUE               VARCHAR(512)    NOT NULL,
    
    CONSTRAINT PK_DIGEST PRIMARY KEY (DIGEST_ID),
    CONSTRAINT FK_DIGEST_ALGORITHM_ID FOREIGN KEY (ALGORITHM_ID) REFERENCES ALGORITHM (ALGORITHM_ID) ON UPDATE CASCADE,
    CONSTRAINT FK_DIGEST_RESULT_BUNDLE_ID FOREIGN KEY (RESULT_BUNDLE_ID) REFERENCES RESULT_BUNDLE (RESULT_BUNDLE_ID) ON DELETE CASCADE,
    
    CONSTRAINT CHK_DIGEST_HEX_VALUE_VALID CHECK (HEX_VALUE ~* '^([0-9]|[a-e])*$'),
    CONSTRAINT CHK_DIGEST_HEX_VALUE_LENGTH CHECK (LENGTH(HEX_VALUE) > 0)
);

CREATE TABLE TEXT_DIGEST (
    DIGEST_ID               INTEGER         NOT NULL,
    SOURCE_TEXT_ID          INTEGER         NOT NULL,

    CONSTRAINT PK_TEXT_DIGEST PRIMARY KEY (DIGEST_ID),
    CONSTRAINT FK_TEXT_DIGEST_DIGEST_ID FOREIGN KEY (DIGEST_ID) REFERENCES DIGEST (DIGEST_ID) ON DELETE CASCADE,
    CONSTRAINT FK_TEXT_DIGEST_SOURCE_TEXT_ID FOREIGN KEY (SOURCE_TEXT_ID) REFERENCES SOURCE_TEXT (SOURCE_TEXT_ID) ON DELETE CASCADE
);

CREATE TABLE FILE_DIGEST (
    DIGEST_ID               INTEGER         NOT NULL,
    SOURCE_FILE_ID          INTEGER         NOT NULL,
    
    CONSTRAINT PK_FILE_DIGEST PRIMARY KEY (DIGEST_ID),
    CONSTRAINT FK_FILE_DIGEST_DIGEST_ID FOREIGN KEY (DIGEST_ID) REFERENCES DIGEST (DIGEST_ID) ON DELETE CASCADE,
    CONSTRAINT FK_FILE_DIGEST_SOURCE_FILE_ID FOREIGN KEY (SOURCE_FILE_ID) REFERENCES SOURCE_FILE (SOURCE_FILE_ID) ON DELETE CASCADE
);

CREATE TABLE EVENT_TYPE(
    EVENT_TYPE_ID           SMALLSERIAL,
    CODE                    VARCHAR(30)     NOT NULL,
    LABEL                   VARCHAR(60)     NOT NULL,
    
    CONSTRAINT PK_EVENT_TYPE PRIMARY KEY (EVENT_TYPE_ID),
    CONSTRAINT AK_EVENT_TYPE_CODE UNIQUE (CODE),
    CONSTRAINT AK_EVENT_TYPE_LABEL UNIQUE (LABEL)
);

CREATE TABLE ENTITY_TYPE(
    ENTITY_TYPE_ID          SMALLSERIAL,
    CODE                    VARCHAR(30)     NOT NULL,
    LABEL                   VARCHAR(60)     NOT NULL,
    
    CONSTRAINT PK_ENTITY_TYPE PRIMARY KEY (ENTITY_TYPE_ID),
    CONSTRAINT AK_ENTITY_TYPE_CODE UNIQUE (CODE),
    CONSTRAINT AK_ENTITY_TYPE_LABEL UNIQUE (LABEL)
);

CREATE TABLE EVENT (
    EVENT_ID                BIGSERIAL,
    EVENT_TYPE_ID           SMALLINT        NOT NULL,
    SOURCE_ITEM_ID          INTEGER         NOT NULL,
    ENTITY_TYPE_ID          SMALLINT        NOT NULL,
    GUEST_ID                INTEGER         NOT NULL,
    OCCURRED_AT             TIMESTAMP(0)    NOT NULL    DEFAULT CURRENT_TIMESTAMP(0),
    
    CONSTRAINT PK_EVENT PRIMARY KEY (EVENT_ID),
    CONSTRAINT FK_EVENT_EVENT_TYPE_ID FOREIGN KEY (EVENT_TYPE_ID) REFERENCES EVENT_TYPE (EVENT_TYPE_ID) ON UPDATE CASCADE,
    CONSTRAINT FK_EVENT_ENTITY_TYPE_ID FOREIGN KEY (ENTITY_TYPE_ID) REFERENCES ENTITY_TYPE (ENTITY_TYPE_ID) ON UPDATE CASCADE,
    CONSTRAINT FK_EVENT_GUEST_ID FOREIGN KEY (GUEST_ID) REFERENCES GUEST (GUEST_ID),
    
    CONSTRAINT CHK_EVENT_OCCURRED_AT_IN_RANGE CHECK (OCCURRED_AT BETWEEN '1900-01-01 00:00:00' AND '2099-12-31 23:59:59')
);

/*2.2 Removal statements*/
DROP TABLE IF EXISTS ALGORITHM CASCADE;
DROP TABLE IF EXISTS GUEST CASCADE;
DROP TABLE IF EXISTS FILE_TYPE CASCADE;
DROP TABLE IF EXISTS SOURCE_TEXT CASCADE;
DROP TABLE IF EXISTS SOURCE_FILE CASCADE;
DROP TABLE IF EXISTS RESULT_BUNDLE CASCADE;
DROP TABLE IF EXISTS DIGEST CASCADE;
DROP TABLE IF EXISTS TEXT_DIGEST CASCADE;
DROP TABLE IF EXISTS FILE_DIGEST CASCADE;
DROP TABLE IF EXISTS EVENT_TYPE CASCADE;
DROP TABLE IF EXISTS ENTITY_TYPE CASCADE;
DROP TABLE IF EXISTS EVENT CASCADE;


/*3. DDL - Indices*/
/*3.1 Primary indices (foreign keys etc.)*/
/*3.1.1 Creation statements*/
CREATE INDEX IDX_SOURCE_FILE_FILE_TYPE_ID ON SOURCE_FILE USING BTREE (FILE_TYPE_ID);

CREATE INDEX IDX_RESULT_BUNDLE_GUEST_ID ON RESULT_BUNDLE USING BTREE (GUEST_ID);

CREATE INDEX IDX_DIGEST_ALGORITHM_ID ON DIGEST USING BTREE (ALGORITHM_ID);
CREATE INDEX IDX_DIGEST_RESULT_BUNDLE_ID ON DIGEST USING BTREE (RESULT_BUNDLE_ID);

CREATE INDEX IDX_TEXT_DIGEST_SOURCE_TEXT_ID ON TEXT_DIGEST USING BTREE (SOURCE_TEXT_ID);

CREATE INDEX IDX_FILE_DIGEST_SOURCE_FILE_ID ON FILE_DIGEST USING BTREE (SOURCE_FILE_ID);

CREATE INDEX IDX_EVENT_EVENT_TYPE_ID ON EVENT USING BTREE (EVENT_TYPE_ID);
CREATE INDEX IDX_EVENT_SOURCE_ITEM_ID ON EVENT USING BTREE (SOURCE_ITEM_ID);
CREATE INDEX IDX_EVENT_ENTITY_TYPE_ID ON EVENT USING BTREE (ENTITY_TYPE_ID);
CREATE INDEX IDX_EVENT_GUEST_ID ON EVENT USING BTREE (GUEST_ID);

/*3.1.2 Removal statements*/
DROP INDEX IF EXISTS IDX_SOURCE_FILE_FILE_TYPE_ID;
DROP INDEX IF EXISTS IDX_RESULT_BUNDLE_GUEST_ID;
DROP INDEX IF EXISTS IDX_DIGEST_ALGORITHM_ID;
DROP INDEX IF EXISTS IDX_DIGEST_RESULT_BUNDLE_ID;
DROP INDEX IF EXISTS IDX_TEXT_DIGEST_SOURCE_TEXT_ID;
DROP INDEX IF EXISTS IDX_FILE_DIGEST_SOURCE_FILE_ID;
DROP INDEX IF EXISTS IDX_EVENT_EVENT_TYPE_ID;
DROP INDEX IF EXISTS IDX_EVENT_SOURCE_ITEM_ID;
DROP INDEX IF EXISTS IDX_EVENT_ENTITY_TYPE_ID;
DROP INDEX IF EXISTS IDX_EVENT_GUEST_ID;


/*3.2 Secondary indices (needs arising from business logic etc.)*/
/*3.2.1 Creation statements*/
CREATE INDEX IDX_FILE_TYPE_LABEL ON FILE_TYPE USING BTREE (LABEL);
CREATE INDEX IDX_RESULT_BUNDLE_VIEW_COUNT ON RESULT_BUNDLE USING BTREE (VIEW_COUNT);
CREATE INDEX IDX_DIGEST_HEX_VALUE ON DIGEST USING BTREE (HEX_VALUE);
CREATE INDEX IDX_EVENT_OCCURRED_AT ON EVENT USING BTREE (OCCURRED_AT);

/*3.2.2 Removal statements*/
DROP INDEX IF EXISTS IDX_FILE_TYPE_LABEL;
DROP INDEX IF EXISTS IDX_RESULT_BUNDLE_VIEW_COUNT;
DROP INDEX IF EXISTS IDX_DIGEST_HEX_VALUE;
DROP INDEX IF EXISTS IDX_EVENT_OCCURRED_AT;


/*4. DDL - Views*/
/*4.1 Creation statements*/
TODO

/*4.1 Removal statements*/
TODO


/*5. Management of initial data*/
/*5.1 Regular tables*/
/*5.1.1 Insertion statements*/
TODO

/*5.1.2 Deletion statements*/
TRUNCATE TABLE GUEST CASCADE;
TRUNCATE TABLE SOURCE_TEXT CASCADE;
TRUNCATE TABLE SOURCE_FILE CASCADE;
TRUNCATE TABLE RESULT_BUNDLE CASCADE;
TRUNCATE TABLE DIGEST CASCADE;
TRUNCATE TABLE TEXT_DIGEST CASCADE;
TRUNCATE TABLE FILE_DIGEST CASCADE;
TRUNCATE TABLE EVENT CASCADE;


/*5.2 Reference tables*/
/*5.2.1 Insertion statements*/
INSERT INTO ALGORITHM (ALGORITHM_ID, NAME, DESIGNER_NAME, DIGEST_LENGTH_BITS, DESCRIPTION) VALUES (1, 'MD2', 'Ronald Linn Rivest', 128, 
'The MD2 Message-Digest Algorithm is a cryptographic hash function developed by Ronald Rivest in 1989. The algorithm is optimized for 
8-bit computers. MD2 is specified in RFC 1319. Although MD2 is no longer considered secure, even as of 2014, it remains in use in 
public key infrastructures as part of certificates generated with MD2 and RSA.');
INSERT INTO ALGORITHM (ALGORITHM_ID, NAME, DESIGNER_NAME, DIGEST_LENGTH_BITS, DESCRIPTION) VALUES (2, 'MD5', 'Ronald Linn Rivest', 128,
'The MD5 message-digest algorithm is a widely used cryptographic hash function producing a 128-bit (16-byte) hash value, typically 
expressed in text format as a 32 digit hexadecimal number. MD5 has been utilized in a wide variety of cryptographic applications, and is 
also commonly used to verify data integrity. MD5 was designed by Ron Rivest in 1991 to replace an earlier hash function, MD4. The source 
code in RFC 1321 contains a "by attribution" RSA license.');
INSERT INTO ALGORITHM (ALGORITHM_ID, NAME, DESIGNER_NAME, DIGEST_LENGTH_BITS, DESCRIPTION) VALUES (3, 'SHA-1', 'National Security Agency', 
160, 'The SHA-1 algorithm is a cryptographic hash function designed by the United States National Security Agency and is a U.S. Federal 
Information Processing Standard published by the United States NIST. SHA-1 produces a 160-bit (20-byte) hash value. A SHA-1 hash value is 
typically rendered as a hexadecimal number, 40 digits long. SHA stands for "secure hash algorithm". The four SHA algorithms are structured 
differently and are named SHA-0, SHA-1, SHA-2, and SHA-3. SHA-0 is the original version of the 160-bit hash function published in 1993 
under the name "SHA": it was not adopted by many applications. Published in 1995, SHA-1 is very similar to SHA-0, but alters the original 
SHA hash specification to correct alleged weaknesses. SHA-2, published in 2001, is significantly different from the SHA-1 hash function. 
SHA-1 is the most widely used of the existing SHA hash functions, and is employed in several widely used applications and protocols.');
INSERT INTO ALGORITHM (ALGORITHM_ID, NAME, DESIGNER_NAME, DIGEST_LENGTH_BITS, DESCRIPTION) VALUES (4, 'SHA-256', 'National Security 
Agency', 256, 'The SHA-256 algorithm is a cryptographic hash function designed by the U.S. National Security Agency (NSA) and published in 
2001 by the NIST as a U.S. Federal Information Processing Standard (FIPS). SHA stands for Secure Hash Algorithm. SHA-256 includes a 
significant number of changes from its predecessor, SHA-1. In 2005, security flaws were identified in SHA-1, namely that a mathematical 
weakness might exist, indicating that a stronger hash function would be desirable. Although SHA-256 bears some similarity to the SHA-1 
algorithm, these attacks have not been successfully extended to SHA-256.');
INSERT INTO ALGORITHM (ALGORITHM_ID, NAME, DESIGNER_NAME, DIGEST_LENGTH_BITS, DESCRIPTION) VALUES (5, 'SHA-384', 'National Security 
Agency', 384, 'The SHA-384 algorithm is a cryptographic hash function designed by the U.S. National Security Agency (NSA) and published in 
2001 by the NIST as a U.S. Federal Information Processing Standard (FIPS). SHA stands for Secure Hash Algorithm. SHA-384 includes a 
significant number of changes from its predecessor, SHA-1. In 2005, security flaws were identified in SHA-1, namely that a mathematical 
weakness might exist, indicating that a stronger hash function would be desirable. Although SHA-384 bears some similarity to the SHA-1 
algorithm, these attacks have not been successfully extended to SHA-384.');
INSERT INTO ALGORITHM (ALGORITHM_ID, NAME, DESIGNER_NAME, DIGEST_LENGTH_BITS, DESCRIPTION) VALUES (6, 'SHA-512', 'National Security 
Agency', 512, 'The SHA-512 algorithm is a cryptographic hash function designed by the U.S. National Security Agency (NSA) and published in 
2001 by the NIST as a U.S. Federal Information Processing Standard (FIPS). SHA stands for Secure Hash Algorithm. SHA-512 includes a 
significant number of changes from its predecessor, SHA-1. In 2005, security flaws were identified in SHA-1, namely that a mathematical 
weakness might exist, indicating that a stronger hash function would be desirable. Although SHA-512 bears some similarity to the SHA-1 
algorithm, these attacks have not been successfully extended to SHA-512.');
UPDATE ALGORITHM AS A
SET DESIGNER_NAME = A_S.NAME_SANITIZED, DESCRIPTION = A_S.DESCRIPTION_SANITIZED
FROM (SELECT ALGORITHM_ID, regexp_replace(DESIGNER_NAME, E'[\\n\\r\\u2028]+', '', 'g') AS NAME_SANITIZED, 
regexp_replace(DESCRIPTION, E'[\\n\\r\\u2028]+', '', 'g') AS DESCRIPTION_SANITIZED FROM ALGORITHM) AS A_S
WHERE A.ALGORITHM_ID = A_S.ALGORITHM_ID;

INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (1, '.3dm', 'OpenNURBS Initiative 3D model');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (2, '.3ds', '3ds Max scene file (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (3, '.3g2', '3GPP2 multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (4, '.3gp', '3GPP multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (5, '.7z', '7-Zip compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (6, '.accdb', 'Microsoft Access database');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (7, '.accde', 'Microsoft Access database (execute only)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (8, '.accdr', 'Microsoft Access database (read-only)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (9, '.accdt', 'Microsoft Access database template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (10, '.ace', 'ACE compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (11, '.adb', 'Ada source code file (body)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (12, '.ads', 'Ada source code file (specification)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (13, '.ai', 'Adobe Illustrator Artwork');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (14, '.aif', 'Audio Interchange File Format');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (15, '.aiff', 'Audio Interchange File Format');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (16, '.apk', 'Android software package');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (17, '.app', 'Mac OS X application');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (18, '.as', 'ActionScript source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (19, '.asf', 'Advanced Systems Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (20, '.asm', 'Assembly source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (21, '.asp', 'Active Server Page');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (22, '.aspx', 'Active Server Page (.NET)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (23, '.asx', 'Advanced Stream Redirector file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (24, '.au', 'Basic audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (25, '.avi', 'Audio Video Interleaved file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (26, '.bak', 'Backup file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (27, '.bat', 'Batch file (MS-DOS)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (28, '.bin', 'Binary disc image');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (29, '.bmp', 'Bitmap image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (30, '.bup', 'Backup file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (31, '.c', 'C/C++ source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (32, '.cab', 'Cabinet compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (33, '.cbl', 'COBOL source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (34, '.cbr', 'Comic Book Archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (35, '.cda', 'CD audio track shortcut');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (36, '.cdl', 'CADKEY Design Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (37, '.cdr', 'CorelDRAW image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (38, '.cer', 'Internet Security Certificate');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (39, '.cfg', 'Configuration file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (40, '.cfm', 'ColdFusion Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (41, '.cfml', 'ColdFusion Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (42, '.cgi', 'Common Gateway Interface script file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (43, '.chm', 'Microsoft Compiled HTML Help');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (44, '.class', 'Java compiled file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (45, '.clj', 'Clojure source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (46, '.com', 'Command file (MS-DOS)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (47, '.cpl', 'Control Panel Applet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (48, '.cpp', 'C++ source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (49, '.crdownload', 'Google Chrome/Chromium partially downloaded file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (50, '.crx', 'Google Chrome/Chromium extension');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (51, '.cs', 'C# source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (52, '.csk', 'ClarisWorks document (Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (53, '.csr', 'Certificate Signing Request');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (54, '.css', 'Cascading Style Sheet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (55, '.csv', 'Comma-Separated Values file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (56, '.cue', 'Cue sheet file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (57, '.cur', 'Windows Cursor Image');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (58, '.cvs', 'Canvas image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (59, '.d', 'D source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (60, '.dart', 'Dart source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (61, '.dat', 'Data file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (62, '.db', 'Database file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (63, '.dbf', 'Database file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (64, '.dcu', 'Delphi compiled file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (65, '.dds', 'DirectDraw Surface file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (66, '.deb', 'Debian software package');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (67, '.dem', 'Video game demo file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (68, '.deskthemepack', 'Windows 8 desktop theme pack');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (69, '.dif', 'Data Interchange Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (70, '.diff', 'Difference file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (71, '.disc', 'Roxio Toast document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (72, '.divx', 'DivX-encoded multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (73, '.dll', 'Dynamic Link Library');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (74, '.dmg', 'Apple disk image');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (75, '.dmp', 'Windows memory dump');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (76, '.doc', 'Microsoft Word document (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (77, '.docb', 'Microsoft Word binary document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (78, '.docm', 'Microsoft Word document (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (79, '.docx', 'Microsoft Word document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (80, '.dot', 'Microsoft Word document template (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (81, '.dotm', 'Microsoft Word document template (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (82, '.dotx', 'Microsoft Word document template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (83, '.drv', 'Driver file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (84, '.dss', 'Digital Speech Standard audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (85, '.dtd', 'Document Type Definition file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (86, '.dvf', 'Sony Digital Voice File');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (87, '.dwg', 'AutoCAD Drawing Database file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (88, '.dxf', 'AutoCAD Drawing Exchange Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (89, '.eml', 'Email message file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (90, '.eps', 'Encapsulated PostScript file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (91, '.erl', 'Erlang source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (92, '.err', 'Error log file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (93, '.exe', 'Executable file (Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (94, '.f', 'Fortran source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (95, '.fla', 'Adobe Flash animation');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (96, '.flv', 'Flash Video file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (97, '.fm3', 'Lotus 1-2-3 spreadsheet formatting');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (98, '.fnt', 'Font file (Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (99, '.fon', 'Font file (legacy, Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (100, '.fs', 'F# source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (101, '.fsx', 'F# source code file (script)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (102, '.gadget', 'Windows gadget');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (103, '.gam', 'Video game save file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (104, '.gbr', 'Gerber design file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (105, '.ged', 'GEDCOM Genealogy Data file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (106, '.gif', 'Graphical Interchange Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (107, '.gitignore', 'Git exclusion file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (108, '.go', 'Go source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (109, '.gpx', 'GPS Exchange Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (110, '.groovy', 'Groovy source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (111, '.gz', 'Gzip compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (112, '.h', 'C/C++/Objective-C header file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (113, '.hgignore', 'Mercurial exclusion file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (114, '.hqx', 'BinHex 4 compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (115, '.hrl', 'Erlang header file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (116, '.hs', 'Haskell script file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (117, '.htm', 'Hypertext Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (118, '.html', 'Hypertext Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (119, '.ical', 'iCalendar calendar file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (120, '.icalendar', 'iCalendar calendar file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (121, '.icns', 'Apple Icon Image');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (122, '.ico', 'Icon image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (123, '.ics', 'iCalendar calendar file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (124, '.iff', 'Interchange File Format');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (125, '.ifo', 'DVD metadata file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (126, '.img', 'Disc image (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (127, '.indd', 'Adobe InDesign document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (128, '.ini', 'Initialization/configuration file (generic, Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (129, '.iso', 'ISO-9660 disc image');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (130, '.jar', 'Java compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (131, '.java', 'Java source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (132, '.jif', 'JPEG Interchange Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (133, '.jpg', 'JPEG image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (134, '.js', 'JavaScript source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (135, '.jse', 'JScript source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (136, '.jsp', 'Java Server Page');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (137, '.jtd', 'Ichitaro 8/9/10/11 document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (138, '.jtt', 'Ichitaro 8/9/10/11 document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (139, '.key', 'Software license key file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (140, '.keychain', 'Mac OS X keychain file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (141, '.kml', 'Keyhole Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (142, '.kmz', 'KML compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (143, '.lib', 'Library file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (144, '.lisp', 'Lisp source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (145, '.lnk', 'File/folder shortcut (Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (146, '.log', 'Log file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (147, '.lst', 'List file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (148, '.lua', 'Lua source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (149, '.m', 'MATLAB source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (150, '.m3u', 'Media playlist file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (151, '.m4a', 'MPEG-4 Audio Layer file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (152, '.m4p', 'iTunes audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (153, '.m4v', 'iTunes multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (154, '.mac', 'MacPaint image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (155, '.map', 'Video game map file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (156, '.max', '3ds Max scene file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (157, '.mcd', 'MathCad document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (158, '.mdb', 'Microsoft Access database (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (159, '.mdf', 'Media Descriptor File');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (160, '.mdl', 'Model file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (161, '.mid', 'MIDI audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (162, '.midi', 'MIDI audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (163, '.mim', 'Multi-Purpose Internet Mail Extensions file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (164, '.mime', 'Multi-Purpose Internet Mail Extensions file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (165, '.ml', 'ML source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (166, '.mm', 'Objective-C++ source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (167, '.mov', 'Apple QuickTime multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (168, '.mp2', 'MPEG-1/2 Layer II audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (169, '.mp3', 'MPEG-1/2 Layer III audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (170, '.mp4', 'MPEG-4 multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (171, '.mpa', 'MPEG-2 Layer II audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (172, '.mpg', 'MPEG-1/2 multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (173, '.mpp', 'Microsoft Project document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (174, '.msg', 'Microsoft Outlook email message file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (175, '.msi', 'Windows installer package');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (176, '.mswmm', 'Windows Movie Maker project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (177, '.mtb', 'MiniTab macro file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (178, '.mtj', 'MiniTab project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (179, '.nes', 'Nintendo (N64) ROM image');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (180, '.nfo', 'Information file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (181, '.oab', 'Microsoft Offline Address Book');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (182, '.obj', 'Wavefront 3D object');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (183, '.odb', 'OpenDocument database');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (184, '.odf', 'OpenDocument formula');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (185, '.odg', 'OpenDocument drawing');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (186, '.odm', 'OpenDocument master document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (187, '.odp', 'OpenDocument presentation');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (188, '.ods', 'OpenDocument spreadsheet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (189, '.odt', 'OpenDocument text document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (190, '.ogg', 'Ogg Vorbis audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (191, '.one', 'Microsoft OneNote document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (192, '.opt', 'Options file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (193, '.ost', 'Microsoft Offline Storage Table');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (194, '.otf', 'OpenType Font');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (195, '.otg', 'OpenDocument drawing template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (196, '.otp', 'OpenDocument presentation template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (197, '.ots', 'OpenDocument spreadsheet template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (198, '.ott', 'OpenDocument text document template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (199, '.oxt', 'Apache OpenOffice/LibreOffice extension');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (200, '.p', 'Object Pascal source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (201, '.pab', 'Microsoft Personal Address Book');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (202, '.pages', 'Apple Pages document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (203, '.part', 'Partially downloaded file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (204, '.pas', 'Delphi source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (205, '.pct', 'Apple Macintosh image file (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (206, '.pdb', 'Program database file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (207, '.pdf', 'Portable Document Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (208, '.php', 'PHP source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (209, '.pif', 'Program Information File');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (210, '.pkg', 'Apple installer package');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (211, '.pl', 'Perl source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (212, '.plugin', 'Plug-in file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (213, '.png', 'Portable Network Graphic');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (214, '.pot', 'Microsoft PowerPoint presentation template (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (215, '.potm', 'Microsoft PowerPoint presentation template (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (216, '.potx', 'Microsoft PowerPoint presentation template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (217, '.pps', 'Microsoft PowerPoint slideshow (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (218, '.ppsm', 'Microsoft PowerPoint slideshow (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (219, '.ppsx', 'Microsoft PowerPoint slideshow');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (220, '.ppt', 'Microsoft PowerPoint presentation (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (221, '.pptm', 'Microsoft PowerPoint presentation (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (222, '.pptx', 'Microsoft PowerPoint presentation');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (223, '.prefs', 'Preferences file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (224, '.prf', 'Microsoft Outlook profile');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (225, '.prj', 'Project file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (226, '.pro', 'Prolog source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (227, '.project', 'Project file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (228, '.properties', 'Properties/configuration file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (229, '.ps', 'PostScript source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (230, '.ps1', 'Windows PowerShell cmdlet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (231, '.psd', 'Adobe Photoshop Document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (232, '.psp', 'PaintShop Pro image file (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (233, '.pspimage', 'PaintShop Pro image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (234, '.pst', 'Microsoft Personal Storage Table');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (235, '.ptb', 'Power Tab project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (236, '.pub', 'Microsoft Publisher document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (237, '.py', 'Python script file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (238, '.pyc', 'Python compiled file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (239, '.qbb', 'QuickBooks Backup');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (240, '.qbw', 'QuickBooks data file (Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (241, '.qt', 'Apple QuickTime multimedia file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (242, '.qxd', 'QuarkXpress Document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (243, '.r', 'R source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (244, '.ra', 'RealAudio container file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (245, '.ram', 'RealAudio metadata file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (246, '.rar', 'WinRAR compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (247, '.rb', 'Ruby source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (248, '.rm', 'RealMedia container file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (249, '.rmvb', 'RealMedia Variable Bitrate container file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (250, '.rom', 'Read-only memory image (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (251, '.rpm', 'Red Hat Package Manager software package');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (252, '.rs', 'Rust source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (253, '.rss', 'Rich Site Summary');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (254, '.rtf', 'Rich Text Format document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (255, '.rv', 'RealVideo container file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (256, '.sav', 'Video game save file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (257, '.sb', 'Scratch project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (258, '.sb2', 'Scratch 2.0 project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (259, '.scala', 'Scala source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (260, '.scm', 'Scheme source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (261, '.sda', 'StarOffice Draw project (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (262, '.sdb', 'StarOffice Base database (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (263, '.sdc', 'StarOffice Calc spreadsheet (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (264, '.sdd', 'StarOffice Impress presentation (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (265, '.sdf', 'Spatial Data Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (266, '.sdw', 'StarOffice Writer document (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (267, '.sea', 'Self-Extracting Archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (268, '.ses', 'Session file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (269, '.settings', 'Settings file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (270, '.sh', 'Shell script file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (271, '.sit', 'StuffIt compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (272, '.sitx', 'StuffIt X compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (273, '.sldm', 'Microsoft PowerPoint slide (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (274, '.sldx', 'Microsoft PowerPoint slide');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (275, '.sll', 'Static link library');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (276, '.sln', 'Microsoft Visual Studio solution file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (277, '.smf', 'StarOffice Math formula (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (278, '.sql', 'Structured Query Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (279, '.srt', 'SubRip subtitle file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (280, '.sub', 'Subtitle file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (281, '.suo', 'Microsoft Visual Studio solution user-specific options file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (282, '.svg', 'Scalable Vector Graphics file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (283, '.swf', 'Shockwave Flash movie');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (284, '.swift', 'Swift source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (285, '.sxc', 'StarOffice Calc spreadsheet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (286, '.sxd', 'StarOffice Draw project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (287, '.sxi', 'StarOffice Impress presentation');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (288, '.sxm', 'StarOffice Math formula');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (289, '.sxw', 'StarOffice Writer document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (290, '.sys', 'System file (Windows)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (291, '.tar', 'Tar uncompressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (292, '.tar.gz', 'Tar/Gzip compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (293, '.tax', 'TurboTax document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (294, '.tax2011', 'TurboTax 2011 document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (295, '.tax2012', 'TurboTax 2012 document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (296, '.tax2013', 'TurboTax 2013 document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (297, '.tax2014', 'TurboTax 2014 document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (298, '.tcl', 'Tcl source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (299, '.tex', 'TeX source document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (300, '.tga', 'Truevision Targa image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (301, '.tgz', 'Tar/Gzip compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (302, '.thm', 'Thumbnail image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (303, '.tif', 'Tagged Image File Format');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (304, '.tiff', 'Tagged Image File Format');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (305, '.tmp', 'Temporary file (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (306, '.toast', 'Roxio Toast disc image');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (307, '.torrent', 'BitTorrent metadata file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (308, '.ttf', 'TrueType Font');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (309, '.txt', 'Plain text file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (310, '.udf', 'Universal Disk Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (311, '.uof', 'Uniform Office Format file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (312, '.uop', 'Uniform Office presentation');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (313, '.uos', 'Uniform Office spreadsheet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (314, '.uot', 'Uniform Office document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (315, '.uud', 'Uuencode decoded file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (316, '.uue', 'Uuencode encoded file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (317, '.v', 'Verilog source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (318, '.vb', 'Visual Basic/VB.NET source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (319, '.vbs', 'VBScript source code file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (320, '.vcard', 'vCard contact file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (321, '.vcd', 'Disc image (generic)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (322, '.vcf', 'vCard contact file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (323, '.vcxproj', 'Visual C++ project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (324, '.vhd', 'VHSIC Hardware Description Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (325, '.vhdl', 'VHSIC Hardware Description Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (326, '.vob', 'DVD Video Object file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (327, '.vsd', 'Microsoft Visio drawing');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (328, '.vst', 'Microsoft Visio template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (329, '.wadl', 'Web Application Description Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (330, '.wav', 'Waveform Audio File Format');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (331, '.wdb', 'Microsoft Works database');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (332, '.wk3', 'Lotus 1-2-3 spreadsheet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (333, '.wks', 'Microsoft Works spreadsheet (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (334, '.wma', 'Windows Media audio file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (335, '.wmf', 'Windows Metafile');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (336, '.wmv', 'Windows Media video file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (337, '.wpd', 'WordPerfect document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (338, '.wps', 'Microsoft Works document');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (339, '.wsdl', 'Web Services Description Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (340, '.wsf', 'Windows Script File');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (341, '.xcodeproj', 'Xcode project');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (342, '.xhtml', 'Extensible Hypertext Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (343, '.xlm', 'Microsoft Excel macro (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (344, '.xlr', 'Microsoft Works spreadsheet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (345, '.xls', 'Microsoft Excel spreadsheet (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (346, '.xlsm', 'Microsoft Excel spreadsheet (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (347, '.xlsx', 'Microsoft Excel spreadsheet');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (348, '.xlt', 'Microsoft Excel spreadsheet template (legacy)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (349, '.xltm', 'Microsoft Excel spreadsheet template (macro-enabled)');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (350, '.xltx', 'Microsoft Excel spreadsheet template');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (351, '.xlw', 'Microsoft Excel workspace');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (352, '.xml', 'Extensible Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (353, '.xsd', 'XML Schema Definition file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (354, '.xsl', 'Extensible Stylesheet Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (355, '.xslt', 'Extensible Stylesheet Language Transformations file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (356, '.xsn', 'Microsoft InfoPath form template file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (357, '.xtm', 'CmapTools Exported Topic Map');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (358, '.yaml', 'YAML Ain''t Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (359, '.yml', 'YAML Ain''t Markup Language file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (360, '.yuv', 'YUV-encoded image file');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (361, '.zip', 'Zip compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (362, '.zipx', 'ZipX compressed archive');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (2147483646, '.unknown', 'Unknown file format');
INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES (2147483647, '.unspecified', 'Unspecified file format');

INSERT INTO EVENT_TYPE (EVENT_TYPE_ID, CODE, LABEL) VALUES (1, 'CREATED', 'Item created');
INSERT INTO EVENT_TYPE (EVENT_TYPE_ID, CODE, LABEL) VALUES (2, 'MODIFIED', 'Item modified');
INSERT INTO EVENT_TYPE (EVENT_TYPE_ID, CODE, LABEL) VALUES (3, 'DELETED', 'Item deleted');
INSERT INTO EVENT_TYPE (EVENT_TYPE_ID, CODE, LABEL) VALUES (4, 'WEBAPP_REQUESTED', 'Guest session started');
INSERT INTO EVENT_TYPE (EVENT_TYPE_ID, CODE, LABEL) VALUES (5, 'PERMALINK_REQUESTED', 'Permalink viewed');

INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (1, 'ALGORITHM', 'Algorithm');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (2, 'GUEST', 'Guest');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (3, 'FILE_TYPE', 'File type');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (4, 'SOURCE_TEXT', 'Source text');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (5, 'SOURCE_FILE', 'Source file');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (6, 'RESULT_BUNDLE', 'Result bundle');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (7, 'DIGEST', 'Digest');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (8, 'TEXT_DIGEST', 'Text-based digest');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (9, 'FILE_DIGEST', 'File-based digest');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (10, 'EVENT_TYPE', 'Event type');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (11, 'ENTITY_TYPE', 'Entity type');
INSERT INTO ENTITY_TYPE (ENTITY_TYPE_ID, CODE, LABEL) VALUES (12, 'EVENT', 'Event');

/*5.2.2 Deletion statements*/
TRUNCATE TABLE ALGORITHM CASCADE;
TRUNCATE TABLE FILE_TYPE CASCADE;
TRUNCATE TABLE EVENT_TYPE CASCADE;
TRUNCATE TABLE ENTITY_TYPE CASCADE;