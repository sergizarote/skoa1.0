SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
CREATE SCHEMA IF NOT EXISTS `Skoa_Bd` ;

-- -----------------------------------------------------
-- Table `Skoa_Bd`.`Dirs`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Skoa_Bd`.`Dirs` (
  `idDirs` INT NOT NULL AUTO_INCREMENT ,
  `DF` VARCHAR(45) NULL ,
  `DG` VARCHAR(45) NULL ,
  PRIMARY KEY (`idDirs`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Skoa_Bd`.`FV`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `Skoa_Bd`.`FV` (
  `idFV` INT NOT NULL AUTO_INCREMENT ,
  `Fecha` DATETIME NULL ,
  `Valor` VARCHAR(45) NULL ,
  `Dirs_idDirs` INT NOT NULL ,
  PRIMARY KEY (`idFV`) ,
  INDEX `fk_FV_Dirs` (`Dirs_idDirs` ASC) ,
  CONSTRAINT `fk_FV_Dirs`
    FOREIGN KEY (`Dirs_idDirs` )
    REFERENCES `Skoa_Bd`.`Dirs` (`idDirs` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
