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
  status: Status;
  tags: Tag[];
  tasks: Task[];
  tests: Test[];
}

export enum Status {
  SUCCESS,
  FAILED,
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
  status: TaskStatus;
  messages: string[];
  isIncremental: boolean;
}

export enum TaskStatus {
  EXECUTED,
  "UP-TO-DATE",
  "FROM-CACHE",
  SKIPPED,
  FAILED,
}

export interface Test {
  name: string;
  className: string;
  durationInMillis: number;
  status: TestStatus;
  failures: TestFailure[];
}

export enum TestStatus {
  SUCCESS,
  FAILED,
  SKIPPED,
}

export interface TestFailure {
  message: string;
  stacktrace: string;
}
