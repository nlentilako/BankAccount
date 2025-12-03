# Git Workflow Documentation

## Branching Strategy

This project uses a feature-branch workflow to organize development:

- `main` - Stable code with completed features
- `feature/refactor` - Refactoring and code cleanup
- `feature/exceptions` - Exception handling implementation
- `feature/testing` - JUnit tests and verification

## Git Commands Used

### Initial Setup
```bash
git init
git add .
git commit -m "Initial commit with Bank Account Management System structure"
```

### Feature Branching
```bash
# Create and switch to feature branch for refactoring
git checkout -b feature/refactor

# Create and switch to feature branch for exceptions
git checkout -b feature/exceptions

# Create and switch to feature branch for testing
git checkout -b feature/testing
```

### Cherry-Pick Usage
```bash
# Switch to main branch
git checkout main

# Cherry-pick specific commits from feature branches
git cherry-pick <commit-hash-of-tests>
git cherry-pick <commit-hash-of-refactoring>
```

### Merge Operations
```bash
# Merge feature branch into main
git checkout main
git merge feature/exceptions

# Push changes to remote repository
git push origin main
```

## Development Commits

The following commits were made during development:

1. "Initial refactoring for clean code"
2. "Added custom exceptions and error handling" 
3. "Added JUnit tests for transactions"
4. "Implemented Statement Generator with error handling"
5. "Final integration and testing"

## Best Practices Followed

- Small, focused commits with descriptive messages
- Feature branches for isolated development
- Cherry-picking specific commits across branches
- Regular testing before merging