package server.book_library.domain.library.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.inventory.repository.LibraryInventoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryInventoryService {
    private final LibraryInventoryRepository libraryInventoryRepository;

    public LibraryInventory registrationInLibrary(LibraryInventory libraryInventory) {
        extracted(libraryInventory);

        return libraryInventoryRepository.save(libraryInventory);
    }

    private void extracted(LibraryInventory libraryInventory) {
        List<LibraryInventory> libraryInventories = findAll();
        boolean isDuplicate = libraryInventories.stream()
                .anyMatch(libraryInventory1 ->
                        libraryInventory1.getBook().getId().equals(libraryInventory.getBook().getId()) &&
                                libraryInventory1.getLibrary().getId().equals(libraryInventory.getLibrary().getId()) &&
                                !libraryInventory1.isDeleted()
                );

        if (isDuplicate) {
            throw new BusinessLogicException(ExceptionCode.LIBRARY_INVENTORY_ALREADY_EXISTS);
        }
    }

    public LibraryInventory updateLibraryInventory(LibraryInventory libraryInventory) {
        LibraryInventory findLibraryInventory = findById(libraryInventory.getId());
        int loanQuantity = findLibraryInventory.getLoanQuantity();
        int totalQuantity = findLibraryInventory.getTotalQuantity();

        if(loanQuantity > totalQuantity) {
            throw new BusinessLogicException(ExceptionCode.QUANTITY_UPDATE_IMPOSSIBLE);
        }
        else {
            Optional.of(libraryInventory.getTotalQuantity()).ifPresent(findLibraryInventory::setTotalQuantity);
            if( loanQuantity == totalQuantity) {
                findLibraryInventory.setLoanStatus(LibraryInventory.LoanStatus.모두대여중);
            }
            return libraryInventoryRepository.save(findLibraryInventory);
        }
    }

    public List<LibraryInventory> findAll() {
        return libraryInventoryRepository.findAll();
    }

    public LibraryInventory deleteLibraryInventory(LibraryInventory libraryInventory) {
        libraryInventory.setDeleted(true);
        return libraryInventoryRepository.save(libraryInventory);
    }

    public void plusLoanQuantity(LibraryInventory libraryInventory){
        libraryInventory.setLoanQuantity(libraryInventory.getLoanQuantity() + 1);
        setLoanStatus(libraryInventory);
    }

    public void minusLoanQuantity(LibraryInventory libraryInventory) {
        libraryInventory.setLoanQuantity(libraryInventory.getLoanQuantity() - 1);
        setLoanStatus(libraryInventory);
    }

    public static void setLoanStatus(LibraryInventory libraryInventory) {
        if(libraryInventory.getLoanQuantity() == libraryInventory.getTotalQuantity()) {
            libraryInventory.setLoanStatus(LibraryInventory.LoanStatus.모두대여중);
        }
        else if (libraryInventory.getLoanQuantity() < libraryInventory.getTotalQuantity()) {
            libraryInventory.setLoanStatus(LibraryInventory.LoanStatus.대여가능);
        }
    }

    public LibraryInventory findById(long id) {
        Optional<LibraryInventory> optionalLibraryInventory = libraryInventoryRepository.findById(id);
        LibraryInventory libraryInventory = optionalLibraryInventory.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LIBRARY_INVENTORY_OUT_OF_STOCK));

        if(libraryInventory.isDeleted()) {
            throw new BusinessLogicException(ExceptionCode.LIBRARY_INVENTORY_IS_DELETED);
        }
        else return libraryInventory;
    }

    public void validLoanStatus(LibraryInventory libraryInventory) {
        LibraryInventory.LoanStatus loanStatus = libraryInventory.getLoanStatus();
        if(loanStatus.equals(LibraryInventory.LoanStatus.모두대여중)) {
            throw new BusinessLogicException(ExceptionCode.LOAN_NOT_ALLOW);
        }
    }
}
