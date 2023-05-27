import React, { useState } from "react";
import useSWR, { mutate } from "swr";
import { useRouter } from "next/router";
import { format } from "date-fns";
import { ReportList, Status } from "@/lib/types";
import {
  Badge,
  Box,
  Button,
  ButtonGroup,
  Card,
  Flex,
  Spacer,
  Table,
  Tbody,
  Td,
  Text,
  Th,
  Thead,
  Tr,
} from "@chakra-ui/react";
import { RepeatIcon } from "@chakra-ui/icons";
import {
  HiChevronDoubleLeft,
  HiChevronDoubleRight,
  HiChevronLeft,
  HiChevronRight,
} from "react-icons/all";

export default function ReportsPage() {
  const router = useRouter();
  const { query } = router;

  const [pageIndex, setPageIndex] = useState(
    query.page ? parseInt(query.page as string) : 0
  );
  const [pageSize, setPageSize] = useState(
    query.size ? parseInt(query.size as string) : 25
  );

  const { data } = useSWR<ReportList>(
    `/api/v1/reports?page=${pageIndex}&size=${pageSize}`,
    (url: string) => fetch(url).then((res) => res.json())
  );
  let totalElements = data ? data.totalElements : 0;
  let totalPages = data ? data.totalPages : 0;
  let reports = data ? data.elements : [];

  const handleRefresh = () => {
    mutate(`/api/v1/reports?page=${pageIndex}&size=${pageSize}`);
  };

  const handlePageChange = (page: number) => {
    const href = `/?page=${page}&size=${pageSize}`;
    router.push(href, href, { shallow: true });
    setPageIndex(page);
  };

  return (
    <main>
      <Card>
        <Flex justifyContent="between" alignItems="baseline">
          <Spacer />
          <Box p={5}>
            <Button
              leftIcon={<RepeatIcon />}
              variant="outline"
              onClick={() => handleRefresh()}
            >
              Refresh
            </Button>
          </Box>
        </Flex>

        <Table className="mt-5">
          <Thead>
            <Tr>
              <Th>Build time</Th>
              <Th>Project</Th>
              <Th>Hostname</Th>
              <Th>Requested tasks</Th>
              <Th>Duration</Th>
              <Th>Status</Th>
            </Tr>
          </Thead>
          <Tbody>
            {reports.length > 0 ? (
              reports.map((report, itemIndex) => (
                <Tr
                  key={`tr-report-${itemIndex}`}
                  className="cursor-pointer hover:bg-gray-100"
                  onClick={() => router.push(`/reports/${report.id}`)}
                >
                  <Td>
                    {format(new Date(report.created), "dd.MM.yyyy HH:mm:ss")}
                  </Td>
                  <Td>{report.project}</Td>
                  <Td>{report.hostname}</Td>
                  <Td>{report.taskExecutionRequest}</Td>
                  <Td>{report.durationInMillis}</Td>
                  <Td>
                    {(() => {
                      switch (report.status) {
                        case Status.FAILED:
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
              <Tr>
                <Td colSpan={5}>No data available</Td>
              </Tr>
            )}
          </Tbody>
        </Table>

        <Flex justifyContent="between" alignItems="baseline">
          <Text p={5}>{totalElements} reports</Text>
          <Spacer />
          <ButtonGroup p={5} isAttached variant="outline">
            <Button
              leftIcon={<HiChevronDoubleLeft />}
              onClick={() => handlePageChange(0)}
              isDisabled={pageIndex == 0}
            ></Button>
            <Button
              leftIcon={<HiChevronLeft />}
              onClick={() => handlePageChange(pageIndex - 1)}
              isDisabled={pageIndex == 0}
            >
              Prev
            </Button>
            <Button
              rightIcon={<HiChevronRight />}
              onClick={() => handlePageChange(pageIndex + 1)}
              isDisabled={pageIndex + 1 == totalPages}
            >
              Next
            </Button>
            <Button
              rightIcon={<HiChevronDoubleRight />}
              onClick={() => handlePageChange(totalPages - 1)}
              isDisabled={pageIndex + 1 == totalPages}
            ></Button>
          </ButtonGroup>
        </Flex>
      </Card>
    </main>
  );
}
