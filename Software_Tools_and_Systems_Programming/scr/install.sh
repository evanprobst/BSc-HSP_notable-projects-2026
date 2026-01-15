#!/bin/bash
INSTALL_DIR="$HOME/.local/bin"
TARGET="gcal"
#--------------------------------------------------------------------------
# 1: Run the make command to build the program using the Makefile
make # Compile with makefile (SH file must be in same directory as make file and .c/.h files)
if [ $? != 0 ]; then # Case make failed
    echo "Compiling failed."
    exit 1
fi
echo "Compiled $TARGET."
#--------------------------------------------------------------------------
# 2: Create the installation directory if it doesn't exist
if [ ! -d "$INSTALL_DIR" ]; then # Case no installation directory
    mkdir -p "$INSTALL_DIR" # Make the directory(-p for nested ones)
    echo "Created installation directory: $INSTALL_DIR"
else # Case it already exists
    echo "Directory $INSTALL_DIR already exists."
fi
#--------------------------------------------------------------------------
# 3: Copy the executable to the installation directory
cp $TARGET "$INSTALL_DIR/" # Copy gcal to install directory
echo "Installed $TARGET to $INSTALL_DIR."
#--------------------------------------------------------------------------
# 4: Append a command to ~/.bashrc to add the installation directory to $PATH
if [[ ":$PATH:" != *":$INSTALL_DIR:"* ]]; then # Case install directory not in PATH
    echo "Adding $INSTALL_DIR to PATH in ~/.bashrc..."
    command="export PATH=\"$INSTALL_DIR:\$PATH\"" # Command to add the installation directory to $PATH
    echo "$command" >> "$HOME/.bashrc"  # Append the command to ~/.bashrc
else
    echo "$INSTALL_DIR is already in PATH."
fi
#--------------------------------------------------------------------------
echo "Installation complete."
