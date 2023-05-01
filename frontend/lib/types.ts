export interface ReportList {
    totalElements: number;
    totalPages: number;
    elements: Report[];
}

export interface Report {
    id: string;
    created: string;
    project: string;
    hostname: string;
    durationInMillis: number;
    tags: Tag[];
    tasks: Task[];
    tests: Test[];
}

export interface Tag {
    key: string;
    value: string;
}

export interface Task {
    path: string;
    startTime: number;
    endTime: number;
    durationInMillis: number;
    status: string;
    messages: string[];
    isIncremental: boolean;
}

export interface Test {
    name: string;
    className: string;
    durationInMillis: number;
    status: string;
    failures: TestFailure[];
}

export interface TestFailure {
    message: string;
    stacktrace: string;
}