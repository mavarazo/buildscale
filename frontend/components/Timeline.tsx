import {
  BarElement,
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  Title,
  Tooltip,
} from "chart.js";
import { Bar } from "react-chartjs-2";
import prettyMilliseconds from "pretty-ms";
import { Box, Card, CardHeader, Heading } from "@chakra-ui/react";
import { Task } from "@/lib/types";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

interface Props {
  tasks: Task[];
}

const Timeline: React.FC<Props> = ({ tasks }) => {
  const firstTask = tasks.reduce((prev, current) =>
    prev.startTime < current.startTime ? prev : current
  );
  const lastTask = tasks.reduce((prev, current) =>
    prev.endTime > current.endTime ? prev : current
  );

  const durationMap: Map<string, number> = new Map<string, number>();
  tasks.forEach((t) => durationMap.set(t.path, t.durationInMillis));

  const footer = (items: any[]) => {
    let duration = 0;

    items.forEach((i) => {
      duration = durationMap.get(i.label) as number;
    });
    return "Duration: " + prettyMilliseconds(duration);
  };

  const options = {
    indexAxis: "y" as const,
    interaction: {
      intersect: false,
    },
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        callbacks: {
          footer: footer,
        },
      },
    },
    maintainAspectRatio: false,
    responsive: true,
    scale: {
      x: {
        min: firstTask.startTime,
        max: lastTask.endTime,
      },
    },
  };

  const data = {
    labels: tasks.map((t) => t.path),
    datasets: [
      {
        data: tasks.map((t) => [
          t.startTime,
          t.endTime === t.startTime ? t.startTime + 1 : t.endTime,
        ]),
        barThickness: 25,
        backgroundColor: "#a855f7",
      },
    ],
  };

  return (
    <Card mt={6}>
      <CardHeader>
        <Heading size="md">Timeline</Heading>
      </CardHeader>
      <Box className="chart-wrapper horizontalBar relative" height="md">
        <Bar className="mx-6" options={options} data={data} />
      </Box>
    </Card>
  );
};

export default Timeline;
