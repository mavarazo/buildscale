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
  taskExecutionRequest: string;
  durationInMillis: number;
  status: Status;
  tags: Tag[];
  tasks: Task[];
  tests: Test[];
}

export enum Status {
  SUCCESS = "SUCCESS",
  FAILED = "FAILED",
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
  EXECUTED = "EXECUTED",
  UP_TO_DATE = "UP-TO-DATE",
  FROM_CACHE = "FROM-CACHE",
  SKIPPED = "SKIPPED",
  FAILED = "FAILED",
}

export interface Test {
  id: string;
  name: string;
  className: string;
  durationInMillis: number;
  status: TestStatus;
  failures: TestFailure[];
}

export enum TestStatus {
  SUCCESS = "SUCCESS",
  FAILED = "FAILED",
  SKIPPED = "SKIPPED",
}

export interface TestFailure {
  message: string;
  stacktrace: string;
}
