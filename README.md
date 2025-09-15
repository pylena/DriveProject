# Simple Drive Project

### OverView:

- DriveService Interface: Provides three storage implementations (S3, Local, Database). The active implementation is selected  using Spring Boot @ConditionalOnProperty.

- Blob Tracking: A class-based tracker is used to keep track of blob operations.

- Authentication: Integrated with Spring Security and JWT. User management was skipped for simplification.
- implement  Authenticating Requests (AWS Signature Version 4) to save/download to S3
- [implement  Authenticating Requests (AWS Signature Version 4) to save/download to S3](https://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-authenticating-requests.html)



