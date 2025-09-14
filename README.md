# Simple Drive Project

### OverView:

- DriveService Interface: Provides three storage implementations (S3, Local, Database). The active implementation is selected  using Spring Boot @ConditionalOnProperty.

- Blob Tracking: A class-based tracker is used to keep track of blob operations.

- Authentication: Integrated with Spring Security and JWT. User management was skipped for simplification.

