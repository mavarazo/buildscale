import {Heading, Table, Tbody, Td, Text, Th, Thead, Tr} from "@chakra-ui/react";
import React from "react";

const dict: Record<string, string> = {}

const Tasks = ({tests}) => {
    const translate = (key: string, ...args) => dict[key] ? dict[key] : key;

    return (
        <div>
            <Heading>Tests details</Heading>
            <Text>{tests.length} test(s) executed</Text>
            <Table>
                <Thead>
                    <Tr>
                        <Th>Class</Th>
                        <Th isNumeric>Duration</Th>
                        <Th>Status</Th>
                    </Tr>
                </Thead>
                <Tbody>
                    {tests.length > 0 ? tests.map((test, itemIndex) => (
                            <Tr key={`tr-test-${itemIndex}`}>
                                <Td>{test.className}</Td>
                                <Td isNumeric>{test.durationInMillis}</Td>
                                <Td>{translate(test.status)}</Td>
                            </Tr>
                        )) :
                        <Tr key="tr-test-0">
                            <Td colSpan={3}>No data</Td>
                        </Tr>
                    }
                </Tbody>
            </Table>
        </div>
    )
        ;
}

export default Tasks;