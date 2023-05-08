import React, { useState } from "react";
import useSWR, { mutate } from "swr";
import { useRouter } from "next/router";
import { format } from "date-fns";
import { ReportList } from "@/lib/types";
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
import {
  ChevronLeftIcon,
  ChevronRightIcon,
  RepeatIcon,
} from "@chakra-ui/icons";

export default function ReportsPage() {
  const router = useRouter();
  const { query } = router;

  const fetcher = (...args) => fetch(...args).then((res) => res.json());

  const [pageIndex, setPageIndex] = useState(
    query.page ? parseInt(query.page) : 0
  );
  const [pageSize, setPageSize] = useState(
    query.size ? parseInt(query.size) : 25
  );

  const { data } = useSWR<ReportList>(
    `/api/reports?page=${pageIndex}&size=${pageSize}`,
    fetcher
  );
  let totalElements = data ? data.totalElements : 0;
  let totalPages = data ? data.totalPages : 0;
  let reports = data ? data.elements : [];

  const handleRefresh = () => {
    mutate(`/api/reports?page=${pageIndex}&size=${pageSize}`);
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
              <Th>Duration</Th>
              <Th>Status</Th>
            </Tr>
          </Thead>
          <Tbody>
            {reports.length > 0 ? (
              reports.map((report, index) => (
                <Tr
                  key={`tr-report-${index}`}
                  className="cursor-pointer hover:bg-gray-100"
                  onClick={() => router.push(`/reports/${report.id}`)}
                >
                  <Td>
                    {format(new Date(report.created), "dd.MM.yyyy HH:mm:ss")}
                  </Td>
                  <Td>{report.project}</Td>
                  <Td>{report.hostname}</Td>
                  <Td>{report.durationInMillis}</Td>
                  <Td>
                    {" "}
                    {
                      {
                        SUCCESS: (
                          <Badge colorScheme="green" borderRadius="5px">
                            success
                          </Badge>
                        ),
                        FAILED: (
                          <Badge colorScheme="red" borderRadius="5px">
                            failed
                          </Badge>
                        ),
                      }[report.status]
                    }
                  </Td>
                </Tr>
              ))
            ) : (
              <Tr>
                <Td colSpan={4}>No data available</Td>
              </Tr>
            )}
          </Tbody>
        </Table>

        <Flex justifyContent="between" alignItems="baseline">
          <Text p={5}>{totalElements} reports</Text>
          <Spacer />
          <ButtonGroup p={5} isAttached variant="outline">
            <Button
              leftIcon={<ChevronLeftIcon />}
              onClick={() => handlePageChange(pageIndex - 1)}
              isDisabled={pageIndex == 0}
            >
              Prev
            </Button>
            <Button
              rightIcon={<ChevronRightIcon />}
              onClick={() => handlePageChange(pageIndex + 1)}
              isDisabled={pageIndex + 1 == totalPages}
            >
              Next
            </Button>
          </ButtonGroup>
        </Flex>
      </Card>
    </main>
  );
}
