import {Table} from "@nextui-org/react";
import {useRouter} from "next/router";

export default function Posts({ reports }) {
  return (
      <Table
          bordered
          shadow={false}
          css={{
              height: "auto",
              minWidth: "100%",
          }}
      >
          <Table.Header>
              <Table.Column>Project</Table.Column>
              <Table.Column>Hostname</Table.Column>
              <Table.Column>Duration</Table.Column>
          </Table.Header>
          <Table.Body>
              {reports.map((report, itemIndex) => (
                  <Table.Row key={`table-body-${itemIndex}`}>
                      <Table.Cell>{report.project}</Table.Cell>
                      <Table.Cell>{report.hostname}</Table.Cell>
                      <Table.Cell>{report.durationInMillis}</Table.Cell>
                  </Table.Row>
              ))}
          </Table.Body>
          <Table.Pagination
              shadow
              noMargin
              align="center"
              rowsPerPage={3}
              onPageChange={(page) => console.log({ page })}
          />
      </Table>
  );
}

export async function getStaticProps({pageable}) {
    const response = await fetch(`http://localhost:15431/v1/reports?page=0&size=30`)
    const data = await response.json()

  return {
    props: {
        reports: data ? data : [],
    }
  };
}