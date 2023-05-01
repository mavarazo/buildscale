import {Heading, Tooltip} from "@chakra-ui/react";
import ms from "ms";
import prettyMilliseconds from "pretty-ms";

const Timeline = ({tasks}) => {
    if (!tasks) return <div>loading...</div>

    const firstTask = tasks.reduce((prev, current) => (prev.startTime < current.startTime) ? prev : current);
    const lastTask = tasks.reduce((prev, current) => (prev.endTime > current.endTime) ? prev : current);

    const relativeStartTime = (startTime: number) => {
        const result = ((startTime - firstTask.startTime) / (lastTask.endTime - firstTask.startTime)) * 100;
        console.log(`start: ${result}`);
        return result;
    };
    const relativeEndTime = (endTime: number) => {
        const result = ((endTime - firstTask.startTime) / (lastTask.endTime - firstTask.startTime)) * 100;
        console.log(`end: ${result}`);
        return result;
    };

    return (
        <div>
            <Heading>Task execution order</Heading>
            {tasks.map((task) => (
                <div className="grid grid-cols-12 relative overflow-hidden">
                    <div className="col-span-2 m-2 whitespace-nowrap">
                        {task.path}
                    </div>
                    <Tooltip label={`${task.durationInMillis}ms`}>
                        <div className="col-span-10 py-2 my-2 bg-purple-500 rounded text-center" style={{
                            marginLeft: `${relativeStartTime(task.startTime)}%`,
                            width: `${relativeEndTime(task.endTime)}%`
                        }}>
                        </div>
                    </Tooltip>
                </div>
            ))
            }
        </div>
    );
}

export default Timeline;