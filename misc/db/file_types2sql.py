
"""
Project:            hashly*/
File description:   A script for parsing the file format data stored in 'file_types.csv' into a
                    batch of (individual) SQL INSERT statements.*/
Author:             Priidu Neemre (priidu@neemre.com)*/
Last modified:      2014-10-07 20:28:24
"""


import sys
import os
import getopt
import re
import collections

HELP_SHORTOPT = '-h'
INPUT_SHORTOPT = '-i'
OUTPUT_SHORTOPT = '-o'

TABLE_NAME = 'FILE_TYPE'
ID_START_WITH = 3
EXTENSION_ITEM_INDEX = 0
LABEL_ITEM_INDEX = 1


#[START] METHOD DECLARATIONS
def getFilename():
    return os.path.basename(__file__)

def isFileExists(filePath):
    if os.path.isfile(filePath):
        return True
    else:
        return False

def removeFile(filePath):
    os.remove(filePath)

def readAll(filePath):
    srcFile = open(filePath, 'rb')
    inputData = srcFile.read()
    srcFile.close()
    return inputData

def writeAll(filePath, outputList):
    destFile = open(filePath, 'wb')
    for outputItem in outputList:
        if outputItem == outputList[len(outputList) - 1]:
            destFile.write(outputItem)
        else:
            destFile.write(outputItem + '\n')
    destFile.close()

def stripSqlComments(targetData):
    commentEndSep = '*/'
    targetData = targetData[targetData.rfind(commentEndSep) + len(commentEndSep):]
    targetData = targetData.strip()
    return targetData

def parseRows(targetData):
    targetRows = targetData.split('\n')
    for i in range(0, len(targetRows)):
        targetRows[i] = re.sub('(\t)+', ',', targetRows[i])
        targetRows[i] = targetRows[i].strip()
        #Apply PostgreSQL-specific escape sequence ('') to all single quotation marks
        targetRows[i] = targetRows[i].replace('\'', '\'\'')
        targetRows[i] = parseRowCells(targetRows[i])
    return targetRows

def parseRowCells(targetRow):
    targetCells = targetRow.split(',')
    for i in range(0, len(targetCells)):
        targetCells[i] = targetCells[i].strip()
        targetCells[i] = '\'' + targetCells[i] + '\''
    return targetCells

def toInsertStmts(targetRows):
    insertStmts = []
    for i in range(0, len(targetRows)):
        insertStmts.append('INSERT INTO FILE_TYPE (FILE_TYPE_ID, EXTENSION, LABEL) VALUES ('
                           + str(i + ID_START_WITH) + ', ' + targetRows[i][EXTENSION_ITEM_INDEX]
                           + ', ' + targetRows[i][LABEL_ITEM_INDEX] + ');')
    return insertStmts

def getShellArgs(argv):
    ShellArgs = collections.namedtuple('ShellArg', 'inputFilePath outputFilePath')
    inputPathTemp = None
    outputPathTemp = None
    try:
        opts, args = getopt.getopt(argv, 'hi:o:')
    except getopt.GetoptError:
        printHelp(1)
    for opt, arg in opts:
        if opt == HELP_SHORTOPT:
            printHelp(0)
        elif opt == INPUT_SHORTOPT:
            inputPathTemp = arg
        elif opt == OUTPUT_SHORTOPT:
            outputPathTemp = arg
    shellArgs = ShellArgs(inputPathTemp, outputPathTemp)
    if isMandatoryArgsEmpty(shellArgs):
        printHelp(2)
    return shellArgs

def isMandatoryArgsEmpty(shellArgs):
    if(shellArgs.inputFilePath is None) or (shellArgs.outputFilePath is None):
        return True
    else:
        return False

def printHelp(exitCode):
    print getFilename() + ' -i <input-file> -o <output-file>'
    sys.exit(exitCode)
#[END] METHOD DECLARATIONS

#[START] CONTROL FLOW
def main(argv):
    shellArgs = getShellArgs(argv)
    print('Starting script \'' + getFilename() + '\'...\n(input from \'' + shellArgs.inputFilePath
          + '\', output to \'' + shellArgs.outputFilePath + '\')\n-------------------------------'
          + '-----------------------')
    if isFileExists(shellArgs.outputFilePath):
        print('A previously generated output file (\'' + shellArgs.outputFilePath + '\') was found!')
        removeFile(shellArgs.outputFilePath)
        print('Old output file (\'' + shellArgs.outputFilePath + '\') successfully removed!')

    print('Reading from input file \'' + shellArgs.inputFilePath + '\'...')
    fileTypeData = readAll(shellArgs.inputFilePath)

    print('Parsing received data...')
    fileTypeData = stripSqlComments(fileTypeData)
    fileTypeRows = parseRows(fileTypeData)

    print('Forming INSERT statements (for table \'' + TABLE_NAME + '\')...')
    insertStmts = toInsertStmts(fileTypeRows)
    print('Outputting results to file \'' + shellArgs.outputFilePath + '\'...')
    writeAll(shellArgs.outputFilePath, insertStmts)
    print('------------------------------------------------------\nAll done.')
#[END] CONTROL FLOW

if __name__ == '__main__':
    main(sys.argv[1:])