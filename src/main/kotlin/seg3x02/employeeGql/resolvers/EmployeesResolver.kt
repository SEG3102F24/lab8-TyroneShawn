package seg3x02.employeeGql.resolvers

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import seg3x02.employeeGql.resolvers.types.UpdateEmployeeInput
import seg3x02.employeeGql.repository.EmployeeRepository

@Component
class EmployeesResolver(
    private val employeeRepository: EmployeeRepository
) : GraphQLQueryResolver, GraphQLMutationResolver {

    fun getEmployeeById(id: String): Employee? {
        return employeeRepository.findById(id).orElse(null)
    }

    fun getAllEmployees(): List<Employee> {
        return employeeRepository.findAll().toList()
    }

    fun createEmployee(input: CreateEmployeeInput): Employee {
        val employee = Employee(
            name = input.name ?: throw IllegalArgumentException("Name is required"),
            dateOfBirth = input.dateOfBirth ?: throw IllegalArgumentException("Date of birth is required"),
            city = input.city ?: throw IllegalArgumentException("City is required"),
            salary = input.salary ?: throw IllegalArgumentException("Salary is required"),
            gender = input.gender,
            email = input.email
        )
        return employeeRepository.save(employee)
    }

    fun updateEmployee(id: String, input: UpdateEmployeeInput): Employee {
        val existingEmployee = employeeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Employee not found") }

        val updatedEmployee = existingEmployee.copy(
            name = input.name ?: existingEmployee.name,
            dateOfBirth = input.dateOfBirth ?: existingEmployee.dateOfBirth,
            city = input.city ?: existingEmployee.city,
            salary = input.salary ?: existingEmployee.salary,
            gender = input.gender ?: existingEmployee.gender,
            email = input.email ?: existingEmployee.email
        )

        return employeeRepository.save(updatedEmployee)
    }

    fun deleteEmployee(id: String): Boolean {
        if (!employeeRepository.existsById(id)) {
            throw IllegalArgumentException("Employee not found")
        }
        employeeRepository.deleteById(id)
        return true
    }
}
