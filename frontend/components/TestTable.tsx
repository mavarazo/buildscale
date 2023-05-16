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
import { useRouter } from "next/router";
import { Test, TestStatus } from "@/lib/types";

interface Props {
  tests: Test[];
}

const TestTable: React.FC<Props> = ({ tests }) => {
  const router = useRouter();

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
          exectued{" in "}
          <Text fontWeight="bold" as="span">
            {prettyMilliseconds(durationInMillis)}
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
              <Tr
                key={`tr-test-${itemIndex}`}
                className="cursor-pointer hover:bg-gray-100"
                onClick={() => router.push(`/tests/${test.id}`)}
              >
                <Td>{test.name}</Td>
                <Td>{test.className}</Td>
                <Td isNumeric>{test.durationInMillis}</Td>
                <Td>
                  {(() => {
                    switch (test.status) {
                      case TestStatus.SKIPPED:
                        return (
                          <Badge colorScheme="yellow" borderRadius="5px">
                            skipped
                          </Badge>
                        );
                      case TestStatus.FAILED:
                        return (
                          <Badge colorScheme="red" borderRadius="5px">
                            failed
                          </Badge>
                        );
                      default:
                        return (
                          <Badge colorScheme="green" borderRadius="5px">
                            success
                          </Badge>
                        );
                    }
                  })()}
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

export default TestTable;
