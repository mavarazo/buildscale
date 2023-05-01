import React, {useState} from "react";
import useSWR, {mutate} from 'swr'
import React, {useState} from "react";
import useSWR, {mutate} from 'swr'
import {useRouter} from "next/router";
import {format} from "date-fns";
import {ReportList} from "@/lib/types";
import {Box, Button, ButtonGroup, Flex, Spacer, Table, Tbody, Td, Text, Th, Thead, Tr} from "@chakra-ui/react";
import {ChevronLeftIcon, ChevronRightIcon, RepeatIcon} from "@chakra-ui/icons";


export default function ReportsPage() {
    const router = useRouter();

    const fetcher = (...args) => fetch(...args).then(res => res.json())

    const [pageIndex, setPageIndex] = useState(0);
    const [pageSize, setPageSize] = useState(3);

    const {data} = useSWR<ReportList>(`/api/reports?page=${pageIndex}&size=${pageSize}`, fetcher);
    let totalElements = data ? data.totalElements : 0;
    let totalPages = data ? data.totalPages : 0;
    let reports = data ? data.elements : [];

    const handleRefresh = () => {
        mutate(`/api/reports?page=${pageIndex}&size=${pageSize}`);
    }

    return (
        <main>
            <Flex justifyContent="between" alignItems="baseline">
                <Spacer/>
                <Box p={5}>
                    <Button leftIcon={<RepeatIcon/>} variant="outline" onClick={() => handleRefresh()}>
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
                    </Tr>
                </Thead>
                <Tbody>
                    {reports.length > 0 ? reports.map((report, index) => (
                            <Tr key={`tr-report-${index}`} className="cursor-pointer hover:bg-gray-100" onClick={() => router.push(`/reports/${report.id}`)}>
                                <Td>{format(new Date(report.created), 'dd.MM.yyyy HH:mm:ss')}</Td>
                                <Td>{report.project}</Td>
                                <Td>{report.hostname}</Td>
                                <Td>{report.durationInMillis}</Td>
                            </Tr>
                        )) :
                        <Tr>
                            <Td colSpan={4}>No data available</Td>
                        </Tr>}
                </Tbody>
            </Table>

            <Flex justifyContent="between" alignItems="baseline">
                <Text p={5}>
                    {totalElements} reports
                </Text>
                <Spacer/>
                <ButtonGroup p={5} isAttached variant='outline'>
                    <Button leftIcon={<ChevronLeftIcon/>}
                            onClick={() => setPageIndex(pageIndex - 1)}
                            disabled={pageIndex == 0}>Prev</Button>
                    <Button rightIcon={<ChevronRightIcon/>}
                            onClick={() => setPageIndex(pageIndex + 1)}
                            disabled={pageIndex + 1 == totalPages}>Next</Button>
                </ButtonGroup>
            </Flex>
        </main>
    );
}
