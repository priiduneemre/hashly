
/*Project:          hashly*/
/*File description: DDL & DML statements for constructing the application's initial database structure (optimized for PostgreSQL 9.2.3).*/
/*Author:           Priidu Neemre (priidu@neemre.com)*/
/*Last modified:    2014-10-10 12:32:10*/


/*1. DDL - Root-level database objects*/
/*1.1 Creation statements*/
/*(For Windows environments)*/
CREATE DATABASE hashly 
WITH
OWNER priidu_dba
TEMPLATE template0
ENCODING 'UTF8'
LC_COLLATE 'English_United states.1252'
LC_CTYPE 'English_United states.1252'
CONNECTION LIMIT 100;

/*(For Linux environments)*/
CREATE DATABASE hashly
WITH
OWNER priidu_dba
TEMPLATE template0
ENCODING 'UTF8'
LC_COLLATE 'en_US.UTF-8'
LC_CTYPE 'en_US.UTF-8'
CONNECTION LIMIT 100;

/*1.2 Removal statements*/
DROP DATABASE IF EXISTS hashly;


/*2. DDL - Tables*/
/*2.1 Creation statements*/
CREATE TABLE algorithm (
    algorithm_id            SERIAL,
    name                    VARCHAR(10)     NOT NULL,
    designer_name           VARCHAR(255)    NOT NULL,
    digest_length_bits      INTEGER         NOT NULL,
    description             TEXT            NOT NULL,
    
    CONSTRAINT pk_algorithm PRIMARY KEY (algorithm_id),
    CONSTRAINT ak_algorithm_name UNIQUE (name),
    
    CONSTRAINT chk_algorithm_digest_length_bits_in_range CHECK (digest_length_bits BETWEEN 1 AND 4096)
);

CREATE TABLE guest (
    guest_id                SERIAL,
    ip_address              VARCHAR(255)    NOT NULL,
    visit_count             INTEGER         NOT NULL    DEFAULT 1,
    
    CONSTRAINT pk_guest PRIMARY KEY (guest_id),
    CONSTRAINT ak_guest_ip_address UNIQUE (ip_address),
    
    CONSTRAINT chk_guest_visit_count_in_range CHECK (visit_count > 0)
);

CREATE TABLE file_type (
    file_type_id            SERIAL,
    extension               VARCHAR(15)     NOT NULL,
    label                   VARCHAR(255)    NOT NULL,
    
    CONSTRAINT pk_file_type PRIMARY KEY (file_type_id),
    CONSTRAINT ak_file_type_extension UNIQUE (extension),
    
    CONSTRAINT chk_file_type_extension_valid CHECK (extension ~* '^(\.).+$')
);

CREATE TABLE source_text (
    source_text_id          SERIAL,
    contents                TEXT            NOT NULL,
    
    CONSTRAINT pk_soruce_text PRIMARY KEY (source_text_id)
);

CREATE TABLE source_file (
    source_file_id          SERIAL,
    file_type_id            INTEGER         NOT NULL,
    filename                VARCHAR(400)    NOT NULL,
    size_bytes              BIGINT          NOT NULL    DEFAULT 0,
    
    CONSTRAINT pk_source_file PRIMARY KEY (source_file_id),
    CONSTRAINT fk_source_file_file_type_id FOREIGN KEY (file_type_id) REFERENCES file_type (file_type_id) ON UPDATE CASCADE,
    
    CONSTRAINT chk_source_file_filename_not_empty CHECK (filename <> ''),
    CONSTRAINT chk_source_file_size_bytes_in_range CHECK (size_bytes >= 0)
);

CREATE TABLE result_bundle (
    result_bundle_id        SERIAL,
    guest_id                INTEGER         NOT NULL,
    permacode               VARCHAR(32)     NOT NULL,
    view_count              INTEGER         NOT NULL    DEFAULT 0,
    
    CONSTRAINT pk_result_bundle PRIMARY KEY (result_bundle_id),
    CONSTRAINT ak_result_bundle_permacode UNIQUE (permacode),
    CONSTRAINT fk_result_bundle_guest_id FOREIGN KEY (guest_id) REFERENCES guest (guest_id),
    
    CONSTRAINT chk_result_bundle_permacode_valid CHECK (permacode ~* '^([0-9]|[a-z])*$'),
    CONSTRAINT chk_result_bundle_view_count_in_range CHECK (view_count >= 0)
);

CREATE TABLE digest (
    digest_id               SERIAL,
    algorithm_id            INTEGER         NOT NULL,
    result_bundle_id        INTEGER         NOT NULL,
    hex_value               VARCHAR(512)    NOT NULL,
    
    CONSTRAINT pk_digest PRIMARY KEY (digest_id),
    CONSTRAINT fk_digest_algorithm_id FOREIGN KEY (algorithm_id) REFERENCES algorithm (algorithm_id) ON UPDATE CASCADE,
    CONSTRAINT fk_digest_result_bundle_id FOREIGN KEY (result_bundle_id) REFERENCES result_bundle (result_bundle_id) ON DELETE CASCADE,
    
    CONSTRAINT chk_digest_hex_value_valid CHECK (hex_value ~* '^([0-9]|[a-e])*$'),
    CONSTRAINT chk_digest_hex_value_length CHECK (length(hex_value) > 0)
);

CREATE TABLE text_digest (
    digest_id               INTEGER         NOT NULL,
    source_text_id          INTEGER         NOT NULL,

    CONSTRAINT pk_text_digest PRIMARY KEY (digest_id),
    CONSTRAINT fk_text_digest_digest_id FOREIGN KEY (digest_id) REFERENCES digest (digest_id) ON DELETE CASCADE,
    CONSTRAINT fk_text_digest_source_text_id FOREIGN KEY (source_text_id) REFERENCES source_text (source_text_id) ON DELETE CASCADE
);

CREATE TABLE file_digest (
    digest_id               INTEGER         NOT NULL,
    source_file_id          INTEGER         NOT NULL,
    
    CONSTRAINT pk_file_digest PRIMARY KEY (digest_id),
    CONSTRAINT fk_file_digest_digest_id FOREIGN KEY (digest_id) REFERENCES digest (digest_id) ON DELETE CASCADE,
    CONSTRAINT fk_file_digest_source_file_id FOREIGN KEY (source_file_id) REFERENCES source_file (source_file_id) ON DELETE CASCADE
);

CREATE TABLE event_type(
    event_type_id           SMALLSERIAL,
    code                    VARCHAR(30)     NOT NULL,
    label                   VARCHAR(60)     NOT NULL,
    
    CONSTRAINT pk_event_type PRIMARY KEY (event_type_id),
    CONSTRAINT ak_event_type_code UNIQUE (code),
    CONSTRAINT ak_event_type_label UNIQUE (label)
);

CREATE TABLE entity_type(
    entity_type_id          SMALLSERIAL,
    code                    VARCHAR(30)     NOT NULL,
    label                   VARCHAR(60)     NOT NULL,
    
    CONSTRAINT pk_entity_type PRIMARY KEY (entity_type_id),
    CONSTRAINT ak_entity_type_code UNIQUE (code),
    CONSTRAINT ak_entity_type_label UNIQUE (label)
);

CREATE TABLE event (
    event_id                BIGSERIAL,
    event_type_id           SMALLINT        NOT NULL,
    source_item_id          INTEGER         NOT NULL,
    entity_type_id          SMALLINT        NOT NULL,
    guest_id                INTEGER         NOT NULL,
    occurred_at             TIMESTAMP(0)    NOT NULL    DEFAULT CURRENT_TIMESTAMP(0),
    
    CONSTRAINT pk_event PRIMARY KEY (event_id),
    CONSTRAINT fk_event_event_type_id FOREIGN KEY (event_type_id) REFERENCES event_type (event_type_id) ON UPDATE CASCADE,
    CONSTRAINT fk_event_entity_type_id FOREIGN KEY (entity_type_id) REFERENCES entity_type (entity_type_id) ON UPDATE CASCADE,
    CONSTRAINT fk_event_guest_id FOREIGN KEY (guest_id) REFERENCES guest (guest_id),
    
    CONSTRAINT chk_event_occurred_at_in_range CHECK (occurred_at BETWEEN '1900-01-01 00:00:00' AND '2099-12-31 23:59:59')
);

/*2.2 Removal statements*/
DROP TABLE IF EXISTS algorithm CASCADE;
DROP TABLE IF EXISTS guest CASCADE;
DROP TABLE IF EXISTS file_type CASCADE;
DROP TABLE IF EXISTS source_text CASCADE;
DROP TABLE IF EXISTS source_file CASCADE;
DROP TABLE IF EXISTS result_bundle CASCADE;
DROP TABLE IF EXISTS digest CASCADE;
DROP TABLE IF EXISTS text_digest CASCADE;
DROP TABLE IF EXISTS file_digest CASCADE;
DROP TABLE IF EXISTS event_type CASCADE;
DROP TABLE IF EXISTS entity_type CASCADE;
DROP TABLE IF EXISTS event CASCADE;


/*3. DDL - Indices*/
/*3.1 Primary indices (foreign keys etc.)*/
/*3.1.1 Creation statements*/
CREATE INDEX idx_source_file_file_type_id ON source_file USING btree (file_type_id);

CREATE INDEX idx_result_bundle_guest_id ON result_bundle USING btree (guest_id);

CREATE INDEX idx_digest_algorithm_id ON digest USING btree (algorithm_id);
CREATE INDEX idx_digest_result_bundle_id ON digest USING btree (result_bundle_id);

CREATE INDEX idx_text_digest_source_text_id ON text_digest USING btree (source_text_id);

CREATE INDEX idx_file_digest_source_file_id ON file_digest USING btree (source_file_id);

CREATE INDEX idx_event_event_type_id ON event USING btree (event_type_id);
CREATE INDEX idx_event_source_item_id ON event USING btree (source_item_id);
CREATE INDEX idx_event_entity_type_id ON event USING btree (entity_type_id);
CREATE INDEX idx_event_guest_id ON event USING btree (guest_id);

/*3.1.2 Removal statements*/
DROP INDEX IF EXISTS idx_source_file_file_type_id;
DROP INDEX IF EXISTS idx_result_bundle_guest_id;
DROP INDEX IF EXISTS idx_digest_algorithm_id;
DROP INDEX IF EXISTS idx_digest_result_bundle_id;
DROP INDEX IF EXISTS idx_text_digest_source_text_id;
DROP INDEX IF EXISTS idx_file_digest_source_file_id;
DROP INDEX IF EXISTS idx_event_event_type_id;
DROP INDEX IF EXISTS idx_event_source_item_id;
DROP INDEX IF EXISTS idx_event_entity_type_id;
DROP INDEX IF EXISTS idx_event_guest_id;


/*3.2 Secondary indices (needs arising from business logic etc.)*/
/*3.2.1 Creation statements*/
CREATE INDEX idx_file_type_label ON file_type USING btree (label);
CREATE INDEX idx_result_bundle_view_count ON result_bundle USING btree (view_count);
CREATE INDEX idx_digest_hex_value ON digest USING btree (hex_value);
CREATE INDEX idx_event_occurred_at ON event USING btree (occurred_at);

/*3.2.2 Removal statements*/
DROP INDEX IF EXISTS idx_file_type_label;
DROP INDEX IF EXISTS idx_result_bundle_view_count;
DROP INDEX IF EXISTS idx_digest_hex_value;
DROP INDEX IF EXISTS idx_event_occurred_at;


/*4. DDL - Views*/
/*4.1 Creation statements*/
TODO

/*4.1 Removal statements*/
TODO


/*5. Management of initial data*/
/*5.1 Regular tables*/
/*5.1.1 Insertion statements*/
INSERT INTO guest (guest_id, ip_address, visit_count) VALUES (1, '191.197.107.184', 4);
INSERT INTO guest (guest_id, ip_address, visit_count) VALUES (2, '127.144.152.194', 1);
INSERT INTO guest (guest_id, ip_address, visit_count) VALUES (3, '118.254.189.53', 8);
INSERT INTO guest (guest_id, ip_address, visit_count) VALUES (4, '29.198.17.207', 5);
INSERT INTO guest (guest_id, ip_address, visit_count) VALUES (5, '97.143.89.22', 24);

/*5.1.2 Deletion statements*/
TRUNCATE TABLE guest CASCADE;
TRUNCATE TABLE source_text CASCADE;
TRUNCATE TABLE source_file CASCADE;
TRUNCATE TABLE result_bundle CASCADE;
TRUNCATE TABLE digest CASCADE;
TRUNCATE TABLE text_digest CASCADE;
TRUNCATE TABLE file_digest CASCADE;
TRUNCATE TABLE event CASCADE;


/*5.2 Reference tables*/
/*5.2.1 Insertion statements*/
INSERT INTO algorithm (algorithm_id, name, designer_name, digest_length_bits, description) VALUES (1, 'MD2', 'Ronald Linn Rivest', 128, 
'The MD2 Message-Digest Algorithm is a cryptographic hash function developed by Ronald Rivest in 1989. The algorithm is optimized for 
8-bit computers. MD2 is specified in RFC 1319. Although MD2 is no longer considered secure, even as of 2014, it remains in use in 
public key infrastructures as part of certificates generated with MD2 and RSA.');
INSERT INTO algorithm (algorithm_id, name, designer_name, digest_length_bits, description) VALUES (2, 'MD5', 'Ronald Linn Rivest', 128,
'The MD5 message-digest algorithm is a widely used cryptographic hash function producing a 128-bit (16-byte) hash value, typically 
expressed in text format as a 32 digit hexadecimal number. MD5 has been utilized in a wide variety of cryptographic applications, and is 
also commonly used to verify data integrity. MD5 was designed by Ron Rivest in 1991 to replace an earlier hash function, MD4. The source 
code in RFC 1321 contains a "by attribution" RSA license.');
INSERT INTO algorithm (algorithm_id, name, designer_name, digest_length_bits, description) VALUES (3, 'SHA-1', 'National Security Agency', 
160, 'The SHA-1 algorithm is a cryptographic hash function designed by the United States National Security Agency and is a U.S. Federal 
Information Processing Standard published by the United States NIST. SHA-1 produces a 160-bit (20-byte) hash value. A SHA-1 hash value is 
typically rendered as a hexadecimal number, 40 digits long. SHA stands for "secure hash algorithm". The four SHA algorithms are structured 
differently and are named SHA-0, SHA-1, SHA-2, and SHA-3. SHA-0 is the original version of the 160-bit hash function published in 1993 
under the name "SHA": it was not adopted by many applications. Published in 1995, SHA-1 is very similar to SHA-0, but alters the original 
SHA hash specification to correct alleged weaknesses. SHA-2, published in 2001, is significantly different from the SHA-1 hash function. 
SHA-1 is the most widely used of the existing SHA hash functions, and is employed in several widely used applications and protocols.');
INSERT INTO algorithm (algorithm_id, name, designer_name, digest_length_bits, description) VALUES (4, 'SHA-256', 'National Security 
Agency', 256, 'The SHA-256 algorithm is a cryptographic hash function designed by the U.S. National Security Agency (NSA) and published in 
2001 by the NIST as a U.S. Federal Information Processing Standard (FIPS). SHA stands for Secure Hash Algorithm. SHA-256 includes a 
significant number of changes from its predecessor, SHA-1. In 2005, security flaws were identified in SHA-1, namely that a mathematical 
weakness might exist, indicating that a stronger hash function would be desirable. Although SHA-256 bears some similarity to the SHA-1 
algorithm, these attacks have not been successfully extended to SHA-256.');
INSERT INTO algorithm (algorithm_id, name, designer_name, digest_length_bits, description) VALUES (5, 'SHA-384', 'National Security 
Agency', 384, 'The SHA-384 algorithm is a cryptographic hash function designed by the U.S. National Security Agency (NSA) and published in 
2001 by the NIST as a U.S. Federal Information Processing Standard (FIPS). SHA stands for Secure Hash Algorithm. SHA-384 includes a 
significant number of changes from its predecessor, SHA-1. In 2005, security flaws were identified in SHA-1, namely that a mathematical 
weakness might exist, indicating that a stronger hash function would be desirable. Although SHA-384 bears some similarity to the SHA-1 
algorithm, these attacks have not been successfully extended to SHA-384.');
INSERT INTO algorithm (algorithm_id, name, designer_name, digest_length_bits, description) VALUES (6, 'SHA-512', 'National Security 
Agency', 512, 'The SHA-512 algorithm is a cryptographic hash function designed by the U.S. National Security Agency (NSA) and published in 
2001 by the NIST as a U.S. Federal Information Processing Standard (FIPS). SHA stands for Secure Hash Algorithm. SHA-512 includes a 
significant number of changes from its predecessor, SHA-1. In 2005, security flaws were identified in SHA-1, namely that a mathematical 
weakness might exist, indicating that a stronger hash function would be desirable. Although SHA-512 bears some similarity to the SHA-1 
algorithm, these attacks have not been successfully extended to SHA-512.');
UPDATE algorithm AS a
SET designer_name = a_s.name_sanitized, description = a_s.description_sanitized
FROM (SELECT algorithm_id, regexp_replace(designer_name, E'[\\n\\r\\u2028]+', '', 'g') AS name_sanitized, 
regexp_replace(description, E'[\\n\\r\\u2028]+', '', 'g') AS description_sanitized FROM algorithm) AS a_s
WHERE a.algorithm_id = a_s.algorithm_id;

INSERT INTO file_type (file_type_id, extension, label) VALUES (1, '.3dm', 'OpenNURBS Initiative 3D model');
INSERT INTO file_type (file_type_id, extension, label) VALUES (2, '.3ds', '3ds Max scene file (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (3, '.3g2', '3GPP2 multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (4, '.3gp', '3GPP multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (5, '.7z', '7-Zip compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (6, '.accdb', 'Microsoft Access database');
INSERT INTO file_type (file_type_id, extension, label) VALUES (7, '.accde', 'Microsoft Access database (execute only)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (8, '.accdr', 'Microsoft Access database (read-only)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (9, '.accdt', 'Microsoft Access database template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (10, '.ace', 'ACE compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (11, '.adb', 'Ada source code file (body)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (12, '.ads', 'Ada source code file (specification)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (13, '.ai', 'Adobe Illustrator Artwork');
INSERT INTO file_type (file_type_id, extension, label) VALUES (14, '.aif', 'Audio Interchange File Format');
INSERT INTO file_type (file_type_id, extension, label) VALUES (15, '.aiff', 'Audio Interchange File Format');
INSERT INTO file_type (file_type_id, extension, label) VALUES (16, '.apk', 'Android software package');
INSERT INTO file_type (file_type_id, extension, label) VALUES (17, '.app', 'Mac OS X application');
INSERT INTO file_type (file_type_id, extension, label) VALUES (18, '.as', 'ActionScript source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (19, '.asf', 'Advanced Systems Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (20, '.asm', 'Assembly source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (21, '.asp', 'Active Server Page');
INSERT INTO file_type (file_type_id, extension, label) VALUES (22, '.aspx', 'Active Server Page (.NET)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (23, '.asx', 'Advanced Stream Redirector file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (24, '.au', 'Basic audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (25, '.avi', 'Audio Video Interleaved file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (26, '.bak', 'Backup file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (27, '.bat', 'Batch file (MS-DOS)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (28, '.bin', 'Binary disc image');
INSERT INTO file_type (file_type_id, extension, label) VALUES (29, '.bmp', 'Bitmap image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (30, '.bup', 'Backup file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (31, '.c', 'C/C++ source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (32, '.cab', 'Cabinet compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (33, '.cbl', 'COBOL source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (34, '.cbr', 'Comic Book Archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (35, '.cda', 'CD audio track shortcut');
INSERT INTO file_type (file_type_id, extension, label) VALUES (36, '.cdl', 'CADKEY Design Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (37, '.cdr', 'CorelDRAW image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (38, '.cer', 'Internet Security Certificate');
INSERT INTO file_type (file_type_id, extension, label) VALUES (39, '.cfg', 'Configuration file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (40, '.cfm', 'ColdFusion Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (41, '.cfml', 'ColdFusion Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (42, '.cgi', 'Common Gateway Interface script file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (43, '.chm', 'Microsoft Compiled HTML Help');
INSERT INTO file_type (file_type_id, extension, label) VALUES (44, '.class', 'Java compiled file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (45, '.clj', 'Clojure source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (46, '.com', 'Command file (MS-DOS)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (47, '.cpl', 'Control Panel Applet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (48, '.cpp', 'C++ source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (49, '.crdownload', 'Google Chrome/Chromium partially downloaded file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (50, '.crx', 'Google Chrome/Chromium extension');
INSERT INTO file_type (file_type_id, extension, label) VALUES (51, '.cs', 'C# source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (52, '.csk', 'ClarisWorks document (Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (53, '.csr', 'Certificate Signing Request');
INSERT INTO file_type (file_type_id, extension, label) VALUES (54, '.css', 'Cascading Style Sheet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (55, '.csv', 'Comma-Separated Values file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (56, '.cue', 'Cue sheet file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (57, '.cur', 'Windows Cursor Image');
INSERT INTO file_type (file_type_id, extension, label) VALUES (58, '.cvs', 'Canvas image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (59, '.d', 'D source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (60, '.dart', 'Dart source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (61, '.dat', 'Data file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (62, '.db', 'Database file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (63, '.dbf', 'Database file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (64, '.dcu', 'Delphi compiled file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (65, '.dds', 'DirectDraw Surface file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (66, '.deb', 'Debian software package');
INSERT INTO file_type (file_type_id, extension, label) VALUES (67, '.dem', 'Video game demo file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (68, '.deskthemepack', 'Windows 8 desktop theme pack');
INSERT INTO file_type (file_type_id, extension, label) VALUES (69, '.dif', 'Data Interchange Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (70, '.diff', 'Difference file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (71, '.disc', 'Roxio Toast document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (72, '.divx', 'DivX-encoded multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (73, '.dll', 'Dynamic Link Library');
INSERT INTO file_type (file_type_id, extension, label) VALUES (74, '.dmg', 'Apple disk image');
INSERT INTO file_type (file_type_id, extension, label) VALUES (75, '.dmp', 'Windows memory dump');
INSERT INTO file_type (file_type_id, extension, label) VALUES (76, '.doc', 'Microsoft Word document (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (77, '.docb', 'Microsoft Word binary document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (78, '.docm', 'Microsoft Word document (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (79, '.docx', 'Microsoft Word document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (80, '.dot', 'Microsoft Word document template (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (81, '.dotm', 'Microsoft Word document template (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (82, '.dotx', 'Microsoft Word document template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (83, '.drv', 'Driver file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (84, '.dss', 'Digital Speech Standard audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (85, '.dtd', 'Document Type Definition file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (86, '.dvf', 'Sony Digital Voice File');
INSERT INTO file_type (file_type_id, extension, label) VALUES (87, '.dwg', 'AutoCAD Drawing Database file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (88, '.dxf', 'AutoCAD Drawing Exchange Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (89, '.eml', 'Email message file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (90, '.eps', 'Encapsulated PostScript file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (91, '.erl', 'Erlang source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (92, '.err', 'Error log file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (93, '.exe', 'Executable file (Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (94, '.f', 'Fortran source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (95, '.fla', 'Adobe Flash animation');
INSERT INTO file_type (file_type_id, extension, label) VALUES (96, '.flv', 'Flash Video file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (97, '.fm3', 'Lotus 1-2-3 spreadsheet formatting');
INSERT INTO file_type (file_type_id, extension, label) VALUES (98, '.fnt', 'Font file (Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (99, '.fon', 'Font file (legacy, Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (100, '.fs', 'F# source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (101, '.fsx', 'F# source code file (script)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (102, '.gadget', 'Windows gadget');
INSERT INTO file_type (file_type_id, extension, label) VALUES (103, '.gam', 'Video game save file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (104, '.gbr', 'Gerber design file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (105, '.ged', 'GEDCOM Genealogy Data file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (106, '.gif', 'Graphical Interchange Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (107, '.gitignore', 'Git exclusion file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (108, '.go', 'Go source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (109, '.gpx', 'GPS Exchange Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (110, '.groovy', 'Groovy source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (111, '.gz', 'Gzip compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (112, '.h', 'C/C++/Objective-C header file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (113, '.hgignore', 'Mercurial exclusion file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (114, '.hqx', 'BinHex 4 compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (115, '.hrl', 'Erlang header file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (116, '.hs', 'Haskell script file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (117, '.htm', 'Hypertext Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (118, '.html', 'Hypertext Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (119, '.ical', 'iCalendar calendar file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (120, '.icalendar', 'iCalendar calendar file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (121, '.icns', 'Apple Icon Image');
INSERT INTO file_type (file_type_id, extension, label) VALUES (122, '.ico', 'Icon image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (123, '.ics', 'iCalendar calendar file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (124, '.iff', 'Interchange File Format');
INSERT INTO file_type (file_type_id, extension, label) VALUES (125, '.ifo', 'DVD metadata file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (126, '.img', 'Disc image (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (127, '.indd', 'Adobe InDesign document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (128, '.ini', 'Initialization/configuration file (generic, Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (129, '.iso', 'ISO-9660 disc image');
INSERT INTO file_type (file_type_id, extension, label) VALUES (130, '.jar', 'Java compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (131, '.java', 'Java source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (132, '.jif', 'JPEG Interchange Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (133, '.jpg', 'JPEG image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (134, '.js', 'JavaScript source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (135, '.jse', 'JScript source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (136, '.jsp', 'Java Server Page');
INSERT INTO file_type (file_type_id, extension, label) VALUES (137, '.jtd', 'Ichitaro 8/9/10/11 document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (138, '.jtt', 'Ichitaro 8/9/10/11 document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (139, '.key', 'Software license key file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (140, '.keychain', 'Mac OS X keychain file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (141, '.kml', 'Keyhole Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (142, '.kmz', 'KML compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (143, '.lib', 'Library file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (144, '.lisp', 'Lisp source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (145, '.lnk', 'File/folder shortcut (Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (146, '.log', 'Log file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (147, '.lst', 'List file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (148, '.lua', 'Lua source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (149, '.m', 'MATLAB source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (150, '.m3u', 'Media playlist file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (151, '.m4a', 'MPEG-4 Audio Layer file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (152, '.m4p', 'iTunes audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (153, '.m4v', 'iTunes multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (154, '.mac', 'MacPaint image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (155, '.map', 'Video game map file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (156, '.max', '3ds Max scene file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (157, '.mcd', 'MathCad document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (158, '.mdb', 'Microsoft Access database (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (159, '.mdf', 'Media Descriptor File');
INSERT INTO file_type (file_type_id, extension, label) VALUES (160, '.mdl', 'Model file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (161, '.mid', 'MIDI audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (162, '.midi', 'MIDI audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (163, '.mim', 'Multi-Purpose Internet Mail Extensions file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (164, '.mime', 'Multi-Purpose Internet Mail Extensions file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (165, '.ml', 'ML source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (166, '.mm', 'Objective-C++ source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (167, '.mov', 'Apple QuickTime multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (168, '.mp2', 'MPEG-1/2 Layer II audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (169, '.mp3', 'MPEG-1/2 Layer III audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (170, '.mp4', 'MPEG-4 multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (171, '.mpa', 'MPEG-2 Layer II audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (172, '.mpg', 'MPEG-1/2 multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (173, '.mpp', 'Microsoft Project document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (174, '.msg', 'Microsoft Outlook email message file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (175, '.msi', 'Windows installer package');
INSERT INTO file_type (file_type_id, extension, label) VALUES (176, '.mswmm', 'Windows Movie Maker project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (177, '.mtb', 'MiniTab macro file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (178, '.mtj', 'MiniTab project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (179, '.nes', 'Nintendo (N64) ROM image');
INSERT INTO file_type (file_type_id, extension, label) VALUES (180, '.nfo', 'Information file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (181, '.oab', 'Microsoft Offline Address Book');
INSERT INTO file_type (file_type_id, extension, label) VALUES (182, '.obj', 'Wavefront 3D object');
INSERT INTO file_type (file_type_id, extension, label) VALUES (183, '.odb', 'OpenDocument database');
INSERT INTO file_type (file_type_id, extension, label) VALUES (184, '.odf', 'OpenDocument formula');
INSERT INTO file_type (file_type_id, extension, label) VALUES (185, '.odg', 'OpenDocument drawing');
INSERT INTO file_type (file_type_id, extension, label) VALUES (186, '.odm', 'OpenDocument master document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (187, '.odp', 'OpenDocument presentation');
INSERT INTO file_type (file_type_id, extension, label) VALUES (188, '.ods', 'OpenDocument spreadsheet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (189, '.odt', 'OpenDocument text document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (190, '.ogg', 'Ogg Vorbis audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (191, '.one', 'Microsoft OneNote document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (192, '.opt', 'Options file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (193, '.ost', 'Microsoft Offline Storage Table');
INSERT INTO file_type (file_type_id, extension, label) VALUES (194, '.otf', 'OpenType Font');
INSERT INTO file_type (file_type_id, extension, label) VALUES (195, '.otg', 'OpenDocument drawing template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (196, '.otp', 'OpenDocument presentation template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (197, '.ots', 'OpenDocument spreadsheet template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (198, '.ott', 'OpenDocument text document template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (199, '.oxt', 'Apache OpenOffice/LibreOffice extension');
INSERT INTO file_type (file_type_id, extension, label) VALUES (200, '.p', 'Object Pascal source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (201, '.pab', 'Microsoft Personal Address Book');
INSERT INTO file_type (file_type_id, extension, label) VALUES (202, '.pages', 'Apple Pages document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (203, '.part', 'Partially downloaded file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (204, '.pas', 'Delphi source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (205, '.pct', 'Apple Macintosh image file (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (206, '.pdb', 'Program database file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (207, '.pdf', 'Portable Document Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (208, '.php', 'PHP source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (209, '.pif', 'Program Information File');
INSERT INTO file_type (file_type_id, extension, label) VALUES (210, '.pkg', 'Apple installer package');
INSERT INTO file_type (file_type_id, extension, label) VALUES (211, '.pl', 'Perl source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (212, '.plugin', 'Plug-in file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (213, '.png', 'Portable Network Graphic');
INSERT INTO file_type (file_type_id, extension, label) VALUES (214, '.pot', 'Microsoft PowerPoint presentation template (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (215, '.potm', 'Microsoft PowerPoint presentation template (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (216, '.potx', 'Microsoft PowerPoint presentation template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (217, '.pps', 'Microsoft PowerPoint slideshow (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (218, '.ppsm', 'Microsoft PowerPoint slideshow (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (219, '.ppsx', 'Microsoft PowerPoint slideshow');
INSERT INTO file_type (file_type_id, extension, label) VALUES (220, '.ppt', 'Microsoft PowerPoint presentation (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (221, '.pptm', 'Microsoft PowerPoint presentation (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (222, '.pptx', 'Microsoft PowerPoint presentation');
INSERT INTO file_type (file_type_id, extension, label) VALUES (223, '.prefs', 'Preferences file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (224, '.prf', 'Microsoft Outlook profile');
INSERT INTO file_type (file_type_id, extension, label) VALUES (225, '.prj', 'Project file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (226, '.pro', 'Prolog source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (227, '.project', 'Project file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (228, '.properties', 'Properties/configuration file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (229, '.ps', 'PostScript source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (230, '.ps1', 'Windows PowerShell cmdlet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (231, '.psd', 'Adobe Photoshop Document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (232, '.psp', 'PaintShop Pro image file (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (233, '.pspimage', 'PaintShop Pro image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (234, '.pst', 'Microsoft Personal Storage Table');
INSERT INTO file_type (file_type_id, extension, label) VALUES (235, '.ptb', 'Power Tab project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (236, '.pub', 'Microsoft Publisher document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (237, '.py', 'Python script file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (238, '.pyc', 'Python compiled file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (239, '.qbb', 'QuickBooks Backup');
INSERT INTO file_type (file_type_id, extension, label) VALUES (240, '.qbw', 'QuickBooks data file (Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (241, '.qt', 'Apple QuickTime multimedia file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (242, '.qxd', 'QuarkXpress Document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (243, '.r', 'R source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (244, '.ra', 'RealAudio container file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (245, '.ram', 'RealAudio metadata file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (246, '.rar', 'WinRAR compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (247, '.rb', 'Ruby source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (248, '.rm', 'RealMedia container file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (249, '.rmvb', 'RealMedia Variable Bitrate container file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (250, '.rom', 'Read-only memory image (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (251, '.rpm', 'Red Hat Package Manager software package');
INSERT INTO file_type (file_type_id, extension, label) VALUES (252, '.rs', 'Rust source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (253, '.rss', 'Rich Site Summary');
INSERT INTO file_type (file_type_id, extension, label) VALUES (254, '.rtf', 'Rich Text Format document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (255, '.rv', 'RealVideo container file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (256, '.sav', 'Video game save file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (257, '.sb', 'Scratch project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (258, '.sb2', 'Scratch 2.0 project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (259, '.scala', 'Scala source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (260, '.scm', 'Scheme source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (261, '.sda', 'StarOffice Draw project (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (262, '.sdb', 'StarOffice Base database (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (263, '.sdc', 'StarOffice Calc spreadsheet (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (264, '.sdd', 'StarOffice Impress presentation (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (265, '.sdf', 'Spatial Data Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (266, '.sdw', 'StarOffice Writer document (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (267, '.sea', 'Self-Extracting Archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (268, '.ses', 'Session file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (269, '.settings', 'Settings file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (270, '.sh', 'Shell script file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (271, '.sit', 'StuffIt compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (272, '.sitx', 'StuffIt X compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (273, '.sldm', 'Microsoft PowerPoint slide (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (274, '.sldx', 'Microsoft PowerPoint slide');
INSERT INTO file_type (file_type_id, extension, label) VALUES (275, '.sll', 'Static link library');
INSERT INTO file_type (file_type_id, extension, label) VALUES (276, '.sln', 'Microsoft Visual Studio solution file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (277, '.smf', 'StarOffice Math formula (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (278, '.sql', 'Structured Query Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (279, '.srt', 'SubRip subtitle file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (280, '.sub', 'Subtitle file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (281, '.suo', 'Microsoft Visual Studio solution user-specific options file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (282, '.svg', 'Scalable Vector Graphics file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (283, '.swf', 'Shockwave Flash movie');
INSERT INTO file_type (file_type_id, extension, label) VALUES (284, '.swift', 'Swift source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (285, '.sxc', 'StarOffice Calc spreadsheet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (286, '.sxd', 'StarOffice Draw project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (287, '.sxi', 'StarOffice Impress presentation');
INSERT INTO file_type (file_type_id, extension, label) VALUES (288, '.sxm', 'StarOffice Math formula');
INSERT INTO file_type (file_type_id, extension, label) VALUES (289, '.sxw', 'StarOffice Writer document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (290, '.sys', 'System file (Windows)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (291, '.tar', 'Tar uncompressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (292, '.tar.gz', 'Tar/Gzip compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (293, '.tax', 'TurboTax document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (294, '.tax2011', 'TurboTax 2011 document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (295, '.tax2012', 'TurboTax 2012 document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (296, '.tax2013', 'TurboTax 2013 document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (297, '.tax2014', 'TurboTax 2014 document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (298, '.tcl', 'Tcl source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (299, '.tex', 'TeX source document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (300, '.tga', 'Truevision Targa image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (301, '.tgz', 'Tar/Gzip compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (302, '.thm', 'Thumbnail image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (303, '.tif', 'Tagged Image File Format');
INSERT INTO file_type (file_type_id, extension, label) VALUES (304, '.tiff', 'Tagged Image File Format');
INSERT INTO file_type (file_type_id, extension, label) VALUES (305, '.tmp', 'Temporary file (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (306, '.toast', 'Roxio Toast disc image');
INSERT INTO file_type (file_type_id, extension, label) VALUES (307, '.torrent', 'BitTorrent metadata file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (308, '.ttf', 'TrueType Font');
INSERT INTO file_type (file_type_id, extension, label) VALUES (309, '.txt', 'Plain text file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (310, '.udf', 'Universal Disk Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (311, '.uof', 'Uniform Office Format file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (312, '.uop', 'Uniform Office presentation');
INSERT INTO file_type (file_type_id, extension, label) VALUES (313, '.uos', 'Uniform Office spreadsheet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (314, '.uot', 'Uniform Office document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (315, '.uud', 'Uuencode decoded file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (316, '.uue', 'Uuencode encoded file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (317, '.v', 'Verilog source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (318, '.vb', 'Visual Basic/VB.NET source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (319, '.vbs', 'VBScript source code file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (320, '.vcard', 'vCard contact file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (321, '.vcd', 'Disc image (generic)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (322, '.vcf', 'vCard contact file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (323, '.vcxproj', 'Visual C++ project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (324, '.vhd', 'VHSIC Hardware Description Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (325, '.vhdl', 'VHSIC Hardware Description Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (326, '.vob', 'DVD Video Object file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (327, '.vsd', 'Microsoft Visio drawing');
INSERT INTO file_type (file_type_id, extension, label) VALUES (328, '.vst', 'Microsoft Visio template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (329, '.wadl', 'Web Application Description Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (330, '.wav', 'Waveform Audio File Format');
INSERT INTO file_type (file_type_id, extension, label) VALUES (331, '.wdb', 'Microsoft Works database');
INSERT INTO file_type (file_type_id, extension, label) VALUES (332, '.wk3', 'Lotus 1-2-3 spreadsheet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (333, '.wks', 'Microsoft Works spreadsheet (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (334, '.wma', 'Windows Media audio file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (335, '.wmf', 'Windows Metafile');
INSERT INTO file_type (file_type_id, extension, label) VALUES (336, '.wmv', 'Windows Media video file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (337, '.wpd', 'WordPerfect document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (338, '.wps', 'Microsoft Works document');
INSERT INTO file_type (file_type_id, extension, label) VALUES (339, '.wsdl', 'Web Services Description Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (340, '.wsf', 'Windows Script File');
INSERT INTO file_type (file_type_id, extension, label) VALUES (341, '.xcodeproj', 'Xcode project');
INSERT INTO file_type (file_type_id, extension, label) VALUES (342, '.xhtml', 'Extensible Hypertext Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (343, '.xlm', 'Microsoft Excel macro (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (344, '.xlr', 'Microsoft Works spreadsheet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (345, '.xls', 'Microsoft Excel spreadsheet (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (346, '.xlsm', 'Microsoft Excel spreadsheet (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (347, '.xlsx', 'Microsoft Excel spreadsheet');
INSERT INTO file_type (file_type_id, extension, label) VALUES (348, '.xlt', 'Microsoft Excel spreadsheet template (legacy)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (349, '.xltm', 'Microsoft Excel spreadsheet template (macro-enabled)');
INSERT INTO file_type (file_type_id, extension, label) VALUES (350, '.xltx', 'Microsoft Excel spreadsheet template');
INSERT INTO file_type (file_type_id, extension, label) VALUES (351, '.xlw', 'Microsoft Excel workspace');
INSERT INTO file_type (file_type_id, extension, label) VALUES (352, '.xml', 'Extensible Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (353, '.xsd', 'XML Schema Definition file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (354, '.xsl', 'Extensible Stylesheet Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (355, '.xslt', 'Extensible Stylesheet Language Transformations file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (356, '.xsn', 'Microsoft InfoPath form template file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (357, '.xtm', 'CmapTools Exported Topic Map');
INSERT INTO file_type (file_type_id, extension, label) VALUES (358, '.yaml', 'YAML Ain''t Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (359, '.yml', 'YAML Ain''t Markup Language file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (360, '.yuv', 'YUV-encoded image file');
INSERT INTO file_type (file_type_id, extension, label) VALUES (361, '.zip', 'Zip compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (362, '.zipx', 'ZipX compressed archive');
INSERT INTO file_type (file_type_id, extension, label) VALUES (2147483646, '.unknown', 'Unknown file format');
INSERT INTO file_type (file_type_id, extension, label) VALUES (2147483647, '.unspecified', 'Unspecified file format');

INSERT INTO event_type (event_type_id, code, label) VALUES (1, 'CREATED', 'Item created');
INSERT INTO event_type (event_type_id, code, label) VALUES (2, 'MODIFIED', 'Item modified');
INSERT INTO event_type (event_type_id, code, label) VALUES (3, 'DELETED', 'Item deleted');
INSERT INTO event_type (event_type_id, code, label) VALUES (4, 'WEBAPP_REQUESTED', 'Guest session started');
INSERT INTO event_type (event_type_id, code, label) VALUES (5, 'PERMALINK_REQUESTED', 'Permalink viewed');

INSERT INTO entity_type (entity_type_id, code, label) VALUES (1, 'ALGORITHM', 'Algorithm');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (2, 'GUEST', 'Guest');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (3, 'FILE_TYPE', 'File type');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (4, 'SOURCE_TEXT', 'Source text');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (5, 'SOURCE_FILE', 'Source file');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (6, 'RESULT_BUNDLE', 'Result bundle');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (7, 'DIGEST', 'Digest');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (8, 'TEXT_DIGEST', 'Text-based digest');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (9, 'FILE_DIGEST', 'File-based digest');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (10, 'EVENT_TYPE', 'Event type');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (11, 'ENTITY_TYPE', 'Entity type');
INSERT INTO entity_type (entity_type_id, code, label) VALUES (12, 'EVENT', 'Event');

/*5.2.2 Deletion statements*/
TRUNCATE TABLE algorithm CASCADE;
TRUNCATE TABLE file_type CASCADE;
TRUNCATE TABLE event_type CASCADE;
TRUNCATE TABLE entity_type CASCADE;