import {
  Badge,
  Card,
  CardHeader,
  Heading,
  Table,
  Tbody,
  Td,
  Text,
  Th,
  Thead,
  Tr,
} from "@chakra-ui/react";
import React from "react";
import prettyMilliseconds from "pretty-ms";

const dict: Record<string, string> = {};

const Tasks = ({ tests }) => {
  const translate = (key: string, ...args) => (dict[key] ? dict[key] : key);

  const durationInMillis = tests.reduce(
    (sum, current) => sum + current.durationInMillis,
    0
  );

  return (
    <Card mt={6}>
      <CardHeader>
        <Heading size="md">Tests details</Heading>
        <Text fontSize="sm" color="gray.500" fontWeight="normal">
          <Text fontWeight="bold" as="span">
            {tests.length} {tests.length > 1 ? "tests" : "test"}
          </Text>{" "}
          exectued{" "}
          <Text fontWeight="bold" as="span">
            in {prettyMilliseconds(durationInMillis)}
          </Text>
        </Text>
      </CardHeader>
      <Table>
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Class</Th>
            <Th isNumeric>Duration</Th>
            <Th>Status</Th>
          </Tr>
        </Thead>
        <Tbody>
          {tests.length > 0 ? (
            tests.map((test, itemIndex) => (
              <Tr key={`tr-test-${itemIndex}`}>
                <Td>{test.name}</Td>
                <Td>{test.className}</Td>
                <Td isNumeric>{test.durationInMillis}</Td>
                <Td>
                  {
                    {
                      SUCCESS: (
                        <Badge colorScheme="green" borderRadius="5px">
                          success
                        </Badge>
                      ),
                      SKIPPED: (
                        <Badge colorScheme="yellow" borderRadius="5px">
                          skipped
                        </Badge>
                      ),
                      FAILED: (
                        <Badge colorScheme="red" borderRadius="5px">
                          failed
                        </Badge>
                      ),
                    }[test.status]
                  }
                </Td>
              </Tr>
            ))
          ) : (
            <Tr key="tr-test-0">
              <Td colSpan={3}>No data</Td>
            </Tr>
          )}
        </Tbody>
      </Table>
    </Card>
  );
};

export default Tasks;
