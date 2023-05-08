import { format } from "date-fns";
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

const TaskTable = ({ tasks, durationInMillis }) => {
  return (
    <Card mt={6}>
      <CardHeader>
        <Heading size="md">Task details</Heading>
        <Text fontSize="sm" color="gray.500" fontWeight="normal">
          <Text fontWeight="bold" as="span">
            {tasks.length} {tasks.length > 1 ? "tasks" : "task"}
          </Text>{" "}
          exectued{" "}
          <Text fontWeight="bold" as="span">
            in {prettyMilliseconds(durationInMillis)}
          </Text>
        </Text>
      </CardHeader>
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
          {tasks.length > 0 ? (
            tasks.map((task, itemIndex) => (
              <Tr key={`tr-task-${itemIndex}`}>
                <Td>{task.path}</Td>
                <Td>{format(task.startTime, "HH:mm:ss:SSSS")}</Td>
                <Td>{format(task.endTime, "HH:mm:ss:SSSS")}</Td>
                <Td isNumeric>{task.durationInMillis}</Td>
                <Td>
                  {
                    {
                      EXECUTED: (
                        <Badge colorScheme="green" borderRadius="5px">
                          executed
                        </Badge>
                      ),
                      "UP-TO-DATE": (
                        <Badge borderRadius="5px">up to date</Badge>
                      ),
                      "FROM-CACHE": (
                        <Badge colorScheme="purple" borderRadius="5px">
                          from cache
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
                    }[task.status]
                  }
                </Td>
              </Tr>
            ))
          ) : (
            <Tr key="tr-task-0">
              <Td colSpan={6}>No data</Td>
            </Tr>
          )}
        </Tbody>
      </Table>
    </Card>
  );
};

export default TaskTable;
