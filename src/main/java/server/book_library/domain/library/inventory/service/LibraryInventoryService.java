package server.book_library.domain.library.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.book_library.global.exception.BusinessLogicException;
import server.book_library.global.exception.ExceptionCode;
import server.book_library.domain.library.inventory.entity.LibraryInventory;
import server.book_library.domain.library.inventory.repository.LibraryInventoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryInventoryService {
    private final LibraryInventoryRepository libraryInventoryRepository;

    public LibraryInventory registrationInLibrary(LibraryInventory libraryInventory) {
        return libraryInventoryRepository.save(libraryInventory);
    }

    public void setLoanQuantity(LibraryInventory libraryInventory){
        libraryInventory.setLoanQuantity(libraryInventory.getLoanQuantity() + 1);
        setLoanStatus(libraryInventory);
    }

    public static void setLoanStatus(LibraryInventory libraryInventory) {
        if(libraryInventory.getLoanQuantity() == libraryInventory.getTotalQuantity()) {
            libraryInventory.setLoanStatus(LibraryInventory.LoanStatus.모두대여중);
        }
    }

    public LibraryInventory findById(long id) {
        Optional<LibraryInventory> optionalLibraryInventory = libraryInventoryRepository.findById(id);
        return optionalLibraryInventory.orElseThrow(() -> new BusinessLogicException(ExceptionCode.LIBRARY_INVENTORY_OUT_OF_STOCK));
    }

    public void validLoanStatus(LibraryInventory libraryInventory) {
        LibraryInventory.LoanStatus loanStatus = libraryInventory.getLoanStatus();
        if(loanStatus.equals(LibraryInventory.LoanStatus.모두대여중)) {
            throw new BusinessLogicException(ExceptionCode.LOAN_NOT_ALLOW);
        }
    }
}
