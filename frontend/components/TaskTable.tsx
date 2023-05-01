import {format} from "date-fns";
import ms from "ms";
import {Heading, Table, Tbody, Td, Text, Th, Thead, Tr} from "@chakra-ui/react";
import React from "react";
import prettyMilliseconds from "pretty-ms";

const TaskTable = ({tasks, durationInMillis}) => {
    const translate = (key: string, ...args) => dict[key] ? dict[key] : key;

    return (
        <div>
            <Heading>Task details</Heading>
            <Text>{tasks.length} task(s) executed in {prettyMilliseconds(durationInMillis)}</Text>
            <Table mt={5}>
                <Thead>
                    <Tr>
                        <Th>Path</Th>
                        <Th>Start time</Th>
                        <Th>End time</Th>
                        <Th isNumeric>Duration</Th>
                        <Th>Status</Th>
                    </Tr>
                </Thead>
                <Tbody>
                    {tasks.length > 0 ? tasks.map((task, itemIndex) => (
                            <Tr key={`tr-task-${itemIndex}`}>
                                <Td>{task.path}</Td>
                                <Td>{format(task.startTime, 'HH:mm:ss:SSSS')}</Td>
                                <Td>{format(task.endTime, 'HH:mm:ss:SSSS')}</Td>
                                <Td isNumeric>{task.durationInMillis}</Td>
                                <Td>{task.status}</Td>
                                <Td>{task.isIncremental}</Td>
                            </Tr>
                        )) :
                        <Tr key="tr-task-0">
                            <Td colSpan={6}>No data</Td>
                        </Tr>
                    }
                </Tbody>
            </Table>
        </div>
    );
}

export default TaskTable;